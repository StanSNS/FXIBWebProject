package fxibBackend.controller;


import fxibBackend.exception.MissingParameterException;
import fxibBackend.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static fxibBackend.constants.ActionConst.*;
import static fxibBackend.constants.MappingConstants.COMMUNITY_CONTROLLER_MAPPING;
import static fxibBackend.constants.URLAccessConst.FRONTEND_BASE_URL;

@CrossOrigin(FRONTEND_BASE_URL)
@RequiredArgsConstructor
@RestController
@RequestMapping(COMMUNITY_CONTROLLER_MAPPING)
public class CommunityController {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final CommunityService communityService;

    /**
     * Handles various post requests in the community, such as setting terms and conditions agreement,
     * adding questions, and adding answers.
     *
     * @param action                    The action to perform.
     * @param username                  Username of the requester.
     * @param jwtToken                  JWT token for authentication.
     * @param topic                     (Optional) Topic for the question.
     * @param answerContent             (Optional) Content of the answer.
     * @param questionID                (Optional) ID of the question.
     * @param questionContent           (Optional) Content of the question.
     * @param isAgreedToTermsAndConditions (Optional) Indicates if the user agrees to terms and conditions.
     * @return A ResponseEntity with the result of the requested action or an error response if parameters are missing or action is unsupported.
     */
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping
    public ResponseEntity<?> postRequests(@RequestParam(value = "action") String action,
                                          @RequestParam String username,
                                          @RequestParam String jwtToken,
                                          @RequestParam(required = false) String topic,
                                          @RequestParam(required = false) String answerContent,
                                          @RequestParam(required = false) String questionID,
                                          @RequestParam(required = false) String questionContent,
                                          @RequestParam(required = false) boolean isAgreedToTermsAndConditions) {
        // Handle various post requests based on the provided action
        return switch (action) {
            case SET_AGREE_TO_TERMS_AND_CONDITIONS -> new ResponseEntity<>(communityService.setTermsAndConditions(username, isAgreedToTermsAndConditions, jwtToken), HttpStatus.OK);
            case ADD_QUESTION -> new ResponseEntity<>(communityService.addQuestion(username, questionContent, topic, jwtToken), HttpStatus.OK);
            case ADD_NEW_ANSWER -> new ResponseEntity<>(communityService.addAnswerToTheQuestion(username, answerContent, Long.valueOf(questionID), jwtToken), HttpStatus.OK);
            default -> throw new MissingParameterException();
        };
    }

    /**
     * Handles various delete requests in the community, such as deleting questions and answers.
     *
     * @param action    The action to perform (e.g., DELETE_QUESTION or DELETE_ANSWER).
     * @param jwtToken  JWT token for authentication.
     * @param username  Username of the requester.
     * @param questionID (Optional) ID of the question to delete.
     * @param answerID   (Optional) ID of the answer to delete.
     * @return A ResponseEntity with the result of the requested action or an error response if parameters are missing or action is unsupported.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{action}")
    public ResponseEntity<?> deleteRequest(@PathVariable("action") String action,
                                           @RequestParam String jwtToken,
                                           @RequestParam String username,
                                           @RequestParam(required = false) String questionID,
                                           @RequestParam(required = false) String answerID) {
        // Handle various delete requests based on the provided action
        return switch (action) {
            case DELETE_QUESTION -> new ResponseEntity<>(communityService.deleteQuestion(username, jwtToken, Long.valueOf(questionID)), HttpStatus.OK);
            case DELETE_ANSWER -> new ResponseEntity<>(communityService.deleteAnswer(username, jwtToken, Long.valueOf(answerID)), HttpStatus.OK);
            default -> throw new MissingParameterException();
        };
    }

    /**
     * Handles various get requests in the community, such as retrieving user terms agreement status,
     * all questions, and all topic names.
     *
     * @param action   The action to perform (e.g., GET_USER_AGREED_TO_TERMS_AND_CONDITIONS or GET_ALL_QUESTIONS).
     * @param jwtToken (Optional) JWT token for authentication.
     * @param username (Optional) Username of the requester.
     * @param topic    (Optional) Topic for the request.
     * @return A ResponseEntity with the result of the requested action or an error response if parameters are missing or action is unsupported.
     */
    @GetMapping
    public ResponseEntity<?> getRequests(@RequestParam(value = "action") String action,
                                         @RequestParam(required = false) String jwtToken,
                                         @RequestParam(required = false) String username,
                                         @RequestParam(required = false) String topic) {
        // Handle various get requests based on the provided action
        return switch (action) {
            case GET_USER_AGREED_TO_TERMS_AND_CONDITIONS -> new ResponseEntity<>(communityService.getTermsAndConditions(username, jwtToken), HttpStatus.OK);
            case GET_ALL_QUESTIONS -> new ResponseEntity<>(communityService.getAllQuestions(topic, username, jwtToken), HttpStatus.OK);
            case GET_ALL_TOPIC_NAMES -> new ResponseEntity<>(communityService.getAllTopicsText(), HttpStatus.OK);
            default -> throw new MissingParameterException();
        };
    }

    /**
     * Handles various put requests in the community, such as increasing or decreasing vote counts and marking questions as solved.
     *
     * @param action      The action to perform (e.g., INCREASE_VOTE_COUNT or DECREASE_VOTE_COUNT).
     * @param username    Username of the requester.
     * @param jwtToken    JWT token for authentication.
     * @param answerID    (Optional) ID of the answer for vote count changes.
     * @param questionID  (Optional) ID of the question to mark as solved.
     * @return A ResponseEntity with the result of the requested action or an error response if parameters are missing or action is unsupported.
     */
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping
    public ResponseEntity<?> putRequests(@RequestParam(value = "action") String action,
                                         @RequestParam String username,
                                         @RequestParam String jwtToken,
                                         @RequestParam(required = false) String answerID,
                                         @RequestParam(required = false) String questionID) {
        // Handle various put requests based on the provided action
        return switch (action) {
            case INCREASE_VOTE_COUNT -> new ResponseEntity<>(communityService.increaseAnswerVoteCount(username, Long.valueOf(answerID), jwtToken), HttpStatus.OK);
            case DECREASE_VOTE_COUNT -> new ResponseEntity<>(communityService.decreaseAnswerVoteCount(username, Long.valueOf(answerID), jwtToken), HttpStatus.OK);
            case SOLVE_QUESTION -> new ResponseEntity<>(communityService.setQuestionSolved(username, Long.valueOf(questionID), jwtToken), HttpStatus.OK);
            default -> throw new MissingParameterException();
        };
    }

}