package ControllerTest;

import fxibBackend.controller.CommunityController;
import fxibBackend.dto.CommunityDTOS.AnswerDTO;
import fxibBackend.dto.CommunityDTOS.CommunityAgreeToTermsAndConditionsDTO;
import fxibBackend.dto.CommunityDTOS.QuestionDTO;
import fxibBackend.exception.MissingParameterException;
import fxibBackend.service.CommunityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static fxibBackend.constants.ActionConst.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CommunityControllerTest {

    @Mock
    private CommunityService communityService;
    private CommunityController communityController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        communityController = new CommunityController(communityService);
    }

    @Test
    void testPostRequests() {
        CommunityAgreeToTermsAndConditionsDTO communityAgreeToTermsAndConditionsDTO = new CommunityAgreeToTermsAndConditionsDTO();
        when(communityService.setTermsAndConditions(any(), anyBoolean(), any())).thenReturn(communityAgreeToTermsAndConditionsDTO);

        ResponseEntity<?> response = communityController.postRequests(
                SET_AGREE_TO_TERMS_AND_CONDITIONS, "testUser", "token", null, null, null, null, true);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        QuestionDTO questionDTO = new QuestionDTO();
        when(communityService.addQuestion(any(), any(), any(), any())).thenReturn(questionDTO);
        response = communityController.postRequests(
                ADD_QUESTION, "testUser", null, "Test Question", "Topic", null, null, false);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        AnswerDTO answerDTO = new AnswerDTO();
        when(communityService.addAnswerToTheQuestion(any(), any(), anyLong(), any())).thenReturn(answerDTO);
        response = communityController.postRequests(
                ADD_NEW_ANSWER, "testUser", null, null, null, "1", "Answer", true);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertThrows(MissingParameterException.class, () -> communityController
                .postRequests("InvalidAction", "testUser", "token", null, null, null, null, false));
    }

    @Test
    void testDeleteRequest() {
        when(communityService.deleteQuestion(any(), any(), anyLong())).thenReturn("Question Deleted");
        when(communityService.deleteAnswer(any(), any(), anyLong())).thenReturn("Answer Deleted");

        ResponseEntity<?> response = communityController.deleteRequest(
                DELETE_QUESTION, "token", "testUser", "1", null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Question Deleted", response.getBody());

        response = communityController.deleteRequest(
                DELETE_ANSWER, "token", "testUser", null, "1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Answer Deleted", response.getBody());

        assertThrows(MissingParameterException.class, () -> communityController
                .deleteRequest("InvalidAction", "token", "testUser", null, null));
    }

    @Test
    void testGetRequests() {
        CommunityAgreeToTermsAndConditionsDTO communityAgreeToTermsAndConditionsDTO = new CommunityAgreeToTermsAndConditionsDTO();
        ArrayList<QuestionDTO> questionDTOS = new ArrayList<>();
        ArrayList<String> strings = new ArrayList<>();
        when(communityService.getTermsAndConditions(any(), any())).thenReturn(communityAgreeToTermsAndConditionsDTO);
        when(communityService.getAllQuestions(any(), any(), any())).thenReturn(questionDTOS);
        when(communityService.getAllTopicsText()).thenReturn(strings);

        ResponseEntity<?> response = communityController.getRequests(
                GET_USER_AGREED_TO_TERMS_AND_CONDITIONS, null, "testUser", null);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        response = communityController.getRequests(
                GET_ALL_QUESTIONS, null, "testUser", "Topic");
        assertEquals(HttpStatus.OK, response.getStatusCode());

        response = communityController.getRequests(GET_ALL_TOPIC_NAMES, null, null, null);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertThrows(MissingParameterException.class, () -> communityController
                .getRequests("InvalidAction", null, null, null));
    }

    @Test
    void testPutRequests() {
        AnswerDTO answerDTO = new AnswerDTO();
        QuestionDTO questionDTO = new QuestionDTO();
        when(communityService.increaseAnswerVoteCount(any(), anyLong(), any())).thenReturn(answerDTO);
        when(communityService.decreaseAnswerVoteCount(any(), anyLong(), any())).thenReturn(answerDTO);
        when(communityService.setQuestionSolved(any(), anyLong(), any())).thenReturn(questionDTO);

        ResponseEntity<?> response = communityController.putRequests(
                INCREASE_VOTE_COUNT, "testUser", "token", "1", null);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        response = communityController.putRequests(
                DECREASE_VOTE_COUNT, "testUser", "token", "1", null);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        response = communityController.putRequests(
                SOLVE_QUESTION, "testUser", "token", null, "1");
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertThrows(MissingParameterException.class, () -> communityController
                .putRequests("InvalidAction", "testUser", "token", "1", "1"));
    }


}
