package fxibBackend.service;

import fxibBackend.dto.CommunityDTOS.AnswerDTO;
import fxibBackend.dto.CommunityDTOS.CommunityAgreeToTermsAndConditionsDTO;
import fxibBackend.dto.CommunityDTOS.QuestionDTO;
import fxibBackend.dto.CommunityDTOS.TopicDTO;
import fxibBackend.entity.*;
import fxibBackend.entity.enums.TopicEnum;
import fxibBackend.exception.AccessDeniedException;
import fxibBackend.exception.DataValidationException;
import fxibBackend.exception.InternalErrorException;
import fxibBackend.exception.ResourceNotFoundException;
import fxibBackend.repository.*;
import fxibBackend.repository.TopicEntityRepository;
import fxibBackend.util.ValidateData;
import fxibBackend.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static fxibBackend.constants.ConfigConst.CUSTOM_DATE_FORMAT;
import static fxibBackend.constants.ResponseConst.ANSWER_DELETED_SUCCESSFULLY;
import static fxibBackend.constants.ResponseConst.QUESTION_DELETED_SUCCESSFULLY;

@Service
@RequiredArgsConstructor
public class CommunityService {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final UserEntityRepository userRepository;
    private final ModelMapper modelMapper;
    private final QuestionEntityRepository questionRepository;
    private final AnswerEntityRepository answerRepository;
    private final AnswerLikeEntityRepository answerLikeEntityRepository;
    private final ValidateData validateData;
    private final ValidationUtil validationUtil;
    private final TopicEntityRepository topicEntityRepository;

    /**
     * Sets the terms and conditions agreement status for a user.
     *
     * @param username                     The username of the user.
     * @param isAgreedToTermsAndConditions The new terms and conditions agreement status.
     * @param jwtToken                     The JWT token for user authentication.
     * @return A CommunityAgreeToTermsAndConditionsDTO representing the updated user's status.
     * @throws AccessDeniedException   If user validation fails or the user is banned or has already agreed to terms.
     * @throws DataValidationException If data validation fails.
     */
    public CommunityAgreeToTermsAndConditionsDTO setTermsAndConditions(String username, boolean isAgreedToTermsAndConditions, String jwtToken) {
        // Validate the user with JWT and retrieve their entity.
        UserEntity userEntity = validateData.validateUserWithJWT(username, jwtToken);

        // Check if the user is banned or has already agreed to terms; deny access.
        if (validateData.isUserBanned(userEntity.getRoles()) || userEntity.getAgreedToTerms()) {
            throw new AccessDeniedException();
        }

        // Update the terms and conditions agreement status for the user and save it.
        userEntity.setAgreedToTerms(isAgreedToTermsAndConditions);
        userRepository.save(userEntity);

        // Map the updated user entity to a CommunityAgreeToTermsAndConditionsDTO.
        CommunityAgreeToTermsAndConditionsDTO communityAgreeToTermsAndConditionsDTO = modelMapper.map(userEntity, CommunityAgreeToTermsAndConditionsDTO.class);

        // Validate the generated DTO.
        if (!validationUtil.isValid(communityAgreeToTermsAndConditionsDTO)) {
            throw new DataValidationException();
        }

        return communityAgreeToTermsAndConditionsDTO;
    }

    /**
     * Retrieves the terms and conditions agreement status for a user.
     *
     * @param username The username of the user.
     * @param jwtToken The JWT token for user authentication.
     * @return A CommunityAgreeToTermsAndConditionsDTO representing the user's terms and conditions status.
     * @throws DataValidationException If data validation fails.
     */
    public CommunityAgreeToTermsAndConditionsDTO getTermsAndConditions(String username, String jwtToken) {
        // Validate the user with JWT and retrieve their entity.
        UserEntity userEntity = validateData.validateUserWithJWT(username, jwtToken);

        // Map the user entity to a CommunityAgreeToTermsAndConditionsDTO.
        CommunityAgreeToTermsAndConditionsDTO communityAgreeToTermsAndConditionsDTO = modelMapper.map(userEntity, CommunityAgreeToTermsAndConditionsDTO.class);

        // Validate the generated DTO.
        if (!validationUtil.isValid(communityAgreeToTermsAndConditionsDTO)) {
            throw new DataValidationException();
        }

        return communityAgreeToTermsAndConditionsDTO;
    }

    /**
     * Retrieves a list of questions based on a given topic, username, and JWT token.
     * If the user is not authenticated, the method returns questions without user-specific details.
     * If authenticated, it retrieves questions along with user-specific data and answers.
     *
     * @param topic    The topic for filtering questions.
     * @param username The username of the user (or "null" if not logged in).
     * @param jwtToken The JWT token for user authentication (or "null" if not logged in).
     * @return A list of QuestionDTO objects representing questions and related data.
     * @throws AccessDeniedException   If user validation fails.
     * @throws DataValidationException If data validation fails.
     */
    public List<QuestionDTO> getAllQuestions(String topic, String username, String jwtToken) {
        // If the user is not authenticated, return questions without user-specific details.
        if (username.equals("null") || jwtToken.equals("null")) {
            return getAllQuestionsForUser(topic, null);
        }

        // If authenticated, validate the user with JWT and retrieve questions with user-specific data.
        validateData.validateUserWithJWT(username, jwtToken.substring(7));
        return getAllQuestionsForUser(topic, username);
    }

    /**
     * Retrieves a list of questions for a specific topic and username, including answers and user details.
     *
     * @param topic    The topic for filtering questions.
     * @param username The username of the user.
     * @return A list of QuestionDTO objects with associated answers and user details.
     * @throws DataValidationException If data validation fails.
     */
    private List<QuestionDTO> getAllQuestionsForUser(String topic, String username) {
        // Retrieve questions based on the specified topic and filter out deleted ones.
        List<QuestionEntity> allByTopic = questionRepository
                .findAllByTopicEntityAndDeleted(topicEntityRepository
                        .findTopicEntityByTopicEnum(TopicEnum.getFromText(topic)), false);
        ArrayList<QuestionDTO> dtoList = new ArrayList<>();

        // Iterate through the retrieved questions to create QuestionDTO objects.
        for (QuestionEntity question : allByTopic) {
            // Retrieve user-specific data for the question's author.
            UserEntity userEntityQuestion = getUserEntityByUsername(question.getWriter());

            // Map the QuestionEntity to a QuestionDTO and set user-specific information.
            QuestionDTO questionDTO = modelMapper.map(question, QuestionDTO.class);
            questionDTO.setUserBiography(userEntityQuestion.getBiography());
            questionDTO.setSubscriptionPlan(userEntityQuestion.getSubscription());

            ArrayList<AnswerDTO> answerDTOS = new ArrayList<>();

            // Iterate through the answers associated with the question.
            for (AnswerEntity answer : question.getAnswers()) {
                if (!answer.getDeleted()) {
                    // Retrieve user-specific data for the answer's author.
                    UserEntity userEntityAnswer = getUserEntityByUsername(answer.getWriter());

                    // Map the AnswerEntity to an AnswerDTO and set user-specific information.
                    AnswerDTO answerDTO = modelMapper.map(answer, AnswerDTO.class);
                    answerDTO.setUserBiography(userEntityAnswer.getBiography());
                    answerDTO.setUserSubscriptionPlan(userEntityAnswer.getSubscription());

                    // Check if the current user has liked the answer and set the "liked" flag.
                    if (username != null && answerLikeEntityRepository.existsByAnswerIDAndUsername(answer.getId(), username)) {
                        answerDTO.setLiked(true);
                    }
                    answerDTOS.add(answerDTO);
                }
            }
            questionDTO.setAnswers(answerDTOS);

            // Validate the generated QuestionDTO and add it to the result list.
            if (!validationUtil.isValid(questionDTO)) {
                throw new DataValidationException();
            }
            dtoList.add(questionDTO);
        }
        return dtoList;
    }


    /**
     * Retrieves the user entity by their username.
     *
     * @param username The username of the user.
     * @return The UserEntity associated with the provided username.
     * @throws ResourceNotFoundException if the user with the given username is not found.
     */
    public UserEntity getUserEntityByUsername(String username) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);
        if (userEntityOptional.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        return userEntityOptional.get();
    }

    /**
     * Adds a new question to the system.
     *
     * @param username The username of the user creating the question.
     * @param content  The content of the question.
     * @param topic    The topic of the question.
     * @param jwtToken The JWT token for authentication.
     * @return The created question DTO.
     * @throws ResourceNotFoundException if the topic does not exist.
     * @throws AccessDeniedException     if the user is banned, hasn't agreed to terms, or data validation fails.
     */
    public QuestionDTO addQuestion(String username, String content, String topic, String jwtToken) {
        // Validate the user with JWT and retrieve their entity.
        UserEntity userEntity = validateData.validateUserWithJWT(username, jwtToken);

        // Check if the user is banned or has not agreed to terms; deny access.
        if (validateData.isUserBanned(userEntity.getRoles()) || !userEntity.getAgreedToTerms()) {
            throw new AccessDeniedException();
        }

        // Create a new QuestionEntity with the provided content and topic.
        QuestionEntity question = new QuestionEntity();
        question.setContent(content);
        question.setTopicEntity(topicEntityRepository.findTopicEntityByTopicEnum(TopicEnum.getFromText(topic)));
        question.setAnswers(new ArrayList<>());
        question.setDate(formatLocalDateTimeAsString(LocalDateTime.now()));
        question.setSolved(false);
        question.setWriter(username);
        question.setDeleted(false);

        // Add the question to the user's list of questions, save it, and update the user entity.
        userEntity.getQuestions().add(question);
        questionRepository.save(question);
        userRepository.save(userEntity);

        // Map the added question entity to a QuestionDTO.
        QuestionDTO questionDTO = modelMapper.map(question, QuestionDTO.class);
        questionDTO.setSubscriptionPlan(userEntity.getSubscription());
        questionDTO.setUserBiography(userEntity.getBiography());

        // Validate the generated DTO and associated entities.
        if (!validationUtil.isValid(questionDTO) || !validationUtil.isValid(userEntity) || !validationUtil.isValid(question)) {
            throw new DataValidationException();
        }

        return questionDTO;
    }


    /**
     * Formats a LocalDateTime object as a string using a custom date-time format.
     *
     * @param localDateTime The LocalDateTime object to be formatted.
     * @return A string representation of the LocalDateTime in the specified format.
     */
    private String formatLocalDateTimeAsString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CUSTOM_DATE_FORMAT);
        return localDateTime.format(formatter);
    }

    /**
     * Adds a new answer to a question.
     *
     * @param username   The username of the user adding the answer.
     * @param content    The content of the answer.
     * @param questionID The ID of the question to which the answer is added.
     * @param jwtToken   The JWT token for authentication.
     * @return The created answer DTO.
     * @throws ResourceNotFoundException if the question is not found.
     * @throws AccessDeniedException     if the user is banned, hasn't agreed to terms, or data validation fails.
     */
    public AnswerDTO addAnswerToTheQuestion(String username, String content, Long questionID, String jwtToken) {
        // Validate the user with JWT and retrieve their entity.
        UserEntity userEntity = validateData.validateUserWithJWT(username, jwtToken);

        // Retrieve the question entity by its ID, or throw an exception if not found.
        Optional<QuestionEntity> questionById = questionRepository.findById(questionID);
        if (questionById.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        // Check if the user is banned or has not agreed to terms; deny access.
        if (validateData.isUserBanned(userEntity.getRoles()) || !userEntity.getAgreedToTerms()) {
            throw new AccessDeniedException();
        }

        // Get the question entity to which the answer is added.
        QuestionEntity question = questionById.get();

        // Create a new AnswerEntity with the provided content and user information.
        AnswerEntity answer = new AnswerEntity();
        answer.setContent(content);
        answer.setDate(formatLocalDateTimeAsString(LocalDateTime.now()));
        answer.setWriter(username);
        answer.setVoteCount(0L);
        answer.setDeleted(false);

        // Save the answer and add it to the question's list of answers.
        answerRepository.save(answer);
        question.getAnswers().add(answer);
        questionRepository.save(question);

        // Map the added answer entity to an AnswerDTO.
        AnswerDTO answerDTO = modelMapper.map(answer, AnswerDTO.class);
        answerDTO.setUserSubscriptionPlan(userEntity.getSubscription());
        answerDTO.setUserBiography(userEntity.getBiography());

        // Validate the generated DTO and associated entities.
        if (!validationUtil.isValid(answerDTO) || !validationUtil.isValid(question)) {
            throw new DataValidationException();
        }

        return answerDTO;
    }

    /**
     * Increases the vote count for an answer and records the user's like.
     *
     * @param username The username of the user performing the action.
     * @param answerID The ID of the answer to upvote.
     * @param jwtToken The JWT token for authentication.
     * @return The updated answer with the increased vote count.
     * @throws ResourceNotFoundException if the answer or user is not found.
     * @throws AccessDeniedException     if the user is banned, hasn't agreed to terms, or the answer is deleted.
     */
    public AnswerDTO increaseAnswerVoteCount(String username, Long answerID, String jwtToken) {
        // Validate the user with JWT and retrieve their entity.
        UserEntity userEntity = validateData.validateUserWithJWT(username, jwtToken);

        // Retrieve the answer entity by its ID and verify it's not deleted, or throw an exception if not found.
        Optional<AnswerEntity> answerById = answerRepository.findByIdAndDeleted(answerID, false);
        if (answerById.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        // Check if the user is banned or has not agreed to terms; deny access.
        if (validateData.isUserBanned(userEntity.getRoles()) || !userEntity.getAgreedToTerms()) {
            throw new AccessDeniedException();
        }

        // Get the answer entity for which the vote count is increased.
        AnswerEntity answer = answerById.get();

        // Create an AnswerLikeEntity to track the user's vote and save it.
        AnswerLikeEntity answerLikeEntity = new AnswerLikeEntity();
        answerLikeEntity.setUsername(username);
        answerLikeEntity.setAnswerID(answerID);
        answerLikeEntity.setDeleted(false);
        answerLikeEntityRepository.save(answerLikeEntity);

        // Associate the user with the answer by setting the liked answer.
        userEntity.setLikedAnswer(answerLikeEntity);

        // Increase the vote count of the answer, then save the user and answer entities.
        answer.setVoteCount(answer.getVoteCount() + 1L);
        userRepository.save(userEntity);
        answerRepository.save(answer);

        // Map the updated answer entity to an AnswerDTO.
        return modelMapper.map(answer, AnswerDTO.class);
    }

    /**
     * Decreases the vote count for an answer and removes the user's like.
     *
     * @param username The username of the user performing the action.
     * @param answerID The ID of the answer to downvote.
     * @param jwtToken The JWT token for authentication.
     * @return The updated answer with the decreased vote count.
     * @throws ResourceNotFoundException if the answer, like entity, or user is not found.
     * @throws AccessDeniedException     if the user is banned, hasn't agreed to terms, or the answer is deleted.
     */
    public AnswerDTO decreaseAnswerVoteCount(String username, Long answerID, String jwtToken) {
        // Validate the user with JWT and retrieve their entity.
        UserEntity userEntity = validateData.validateUserWithJWT(username, jwtToken);

        // Retrieve the answer entity by its ID and verify it's not deleted, or throw an exception if not found.
        Optional<AnswerEntity> answerById = answerRepository.findByIdAndDeleted(answerID, false);

        // Check if the user is banned or has not agreed to terms; deny access.
        if (!userEntity.getAgreedToTerms() || validateData.isUserBanned(userEntity.getRoles())) {
            throw new AccessDeniedException();
        }

        // Retrieve the AnswerLikeEntity by answer ID and username to verify the user's previous vote, or throw an exception if not found.
        Optional<AnswerLikeEntity> answerLikeEntityByAnswerIdOptional = answerLikeEntityRepository.getAnswerLikeEntityByAnswerIDAndUsernameAndDeleted(answerID, username, false);

        // If the answer or the user's previous vote is not found, throw a ResourceNotFoundException.
        if (answerById.isEmpty() || answerLikeEntityByAnswerIdOptional.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        // Get the answer entity for which the vote count is decreased.
        AnswerEntity answer = answerById.get();

        // Decrease the vote count of the answer and update it in the repository.
        answer.setVoteCount(answer.getVoteCount() - 1L);
        answerRepository.save(answer);

        // Retrieve the AnswerLikeEntity and user's vote, then disassociate the user from the vote and update the repository.
        AnswerLikeEntity answerLikeEntity = answerLikeEntityByAnswerIdOptional.get();
        userEntity.setLikedAnswer(null);
        userRepository.save(userEntity);
        answerLikeEntityRepository.delete(answerLikeEntity);

        // Map the updated answer entity to an AnswerDTO.
        return modelMapper.map(answer, AnswerDTO.class);
    }

    /**
     * Sets a question as solved by the user who asked the question.
     *
     * @param username   The username of the user marking the question as solved.
     * @param questionID The ID of the question to mark as solved.
     * @param jwtToken   The JWT token for authentication.
     * @return The updated question with the "solved" flag set to true.
     * @throws ResourceNotFoundException if the question or user is not found.
     * @throws AccessDeniedException     if the user is banned, hasn't agreed to terms, or doesn't own the question.
     */
    public QuestionDTO setQuestionSolved(String username, Long questionID, String jwtToken) {
        // Validate the user with JWT and retrieve their entity.
        UserEntity userEntity = validateData.validateUserWithJWT(username, jwtToken);

        // Retrieve the question entity by its ID, or throw an exception if not found.
        Optional<QuestionEntity> questionEntityOptional = questionRepository.findById(questionID);

        // If the question is not found, throw a ResourceNotFoundException.
        if (questionEntityOptional.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        // Get the question entity to be marked as solved.
        QuestionEntity question = questionEntityOptional.get();

        // Check if the user is the writer of the question, has agreed to terms, and is not banned; deny access otherwise.
        if (!question.getWriter().equals(userEntity.getUsername()) || !userEntity.getAgreedToTerms() || validateData.isUserBanned(userEntity.getRoles())) {
            throw new AccessDeniedException();
        }

        // Mark the question as solved and update it in the repository.
        question.setSolved(true);
        questionRepository.save(question);

        // Map the updated question entity to a QuestionDTO.
        return modelMapper.map(question, QuestionDTO.class);
    }

    /**
     * Retrieves a list of all topic names in text format.
     *
     * @return A list of topic names as strings.
     * @throws DataValidationException if data validation fails.
     */
    public List<String> getAllTopicsText() {
        return topicEntityRepository.findAllByOrderByIdAsc().stream().map(topicEntity -> {
            TopicEnum topicEnum = topicEntity.getTopicEnum();
            TopicDTO topicDTO = new TopicDTO(topicEnum.getText());

            // Check if the topic data is valid; if not, throw a DataValidationException.
            if (validationUtil.isValid(topicDTO)) {
                return topicDTO.getName();
            }
            throw new DataValidationException();
        }).collect(Collectors.toList());
    }

    /**
     * Deletes a question with the specified ID if the user is valid and has the necessary permissions.
     * This operation also deletes associated answers.
     *
     * @param username   The username of the user requesting the action.
     * @param jwtToken   The JWT token for authentication.
     * @param questionId The ID of the question to be deleted.
     * @return A success message indicating the question has been deleted.
     * @throws ResourceNotFoundException if the question with the given ID is not found.
     * @throws InternalErrorException    if the user is not valid or does not have the necessary permissions.
     */
    public String deleteQuestion(String username, String jwtToken, Long questionId) {
        if (isUserValidAndAllowed(username, jwtToken)) {
            Optional<QuestionEntity> questionEntity = questionRepository.findById(questionId);

            // If the question is not found, throw a ResourceNotFoundException.
            if (questionEntity.isEmpty()) {
                throw new ResourceNotFoundException();
            }
            QuestionEntity question = questionEntity.get();
            question.setDeleted(true);

            // Iterate through associated answers, set them as deleted, and update the repository.
            for (AnswerEntity currentAnswer : question.getAnswers()) {
                AnswerEntity answer = answerRepository.findById(currentAnswer.getId()).get();
                answer.setDeleted(true);
                answerRepository.save(answer);

                // Find and set associated answer likes as deleted, and update the repository.
                Optional<AnswerLikeEntity> answerLikeEntityOptional = answerLikeEntityRepository.findByAnswerID(answer.getId());
                if (answerLikeEntityOptional.isPresent()) {
                    AnswerLikeEntity answerLikeEntity = answerLikeEntityOptional.get();
                    answerLikeEntity.setDeleted(true);
                    answerLikeEntityRepository.save(answerLikeEntity);
                }
            }

            // Update the question entity in the repository.
            questionRepository.save(question);

            return QUESTION_DELETED_SUCCESSFULLY;
        }

        // Throw an exception if user validation fails or access is denied.
        throw new InternalErrorException();
    }

    /**
     * Deletes an answer with the specified ID if the user is valid and has the necessary permissions.
     *
     * @param username The username of the user requesting the action.
     * @param jwtToken The JWT token for authentication.
     * @param answerID The ID of the answer to be deleted.
     * @return A success message indicating the answer has been deleted.
     * @throws ResourceNotFoundException if the answer with the given ID is not found.
     * @throws InternalErrorException    if the user is not valid or does not have the necessary permissions.
     */
    public String deleteAnswer(String username, String jwtToken, Long answerID) {
        if (isUserValidAndAllowed(username, jwtToken)) {
            Optional<AnswerEntity> answerEntity = answerRepository.findById(answerID);

            // If the answer is not found, throw a ResourceNotFoundException.
            if (answerEntity.isEmpty()) {
                throw new ResourceNotFoundException();
            }
            AnswerEntity answer = answerEntity.get();
            answer.setDeleted(true);

            // Update the answer entity in the repository.
            answerRepository.save(answer);

            // Find and set associated answer likes as deleted, and update the repository.
            Optional<AnswerLikeEntity> answerLikeEntityOptional = answerLikeEntityRepository.getByAnswerID(answer.getId());
            if (answerLikeEntityOptional.isPresent()) {
                AnswerLikeEntity answerLikeEntity = answerLikeEntityOptional.get();
                answerLikeEntity.setDeleted(true);
                answerLikeEntityRepository.save(answerLikeEntity);
            }

            return ANSWER_DELETED_SUCCESSFULLY;
        }

        // Throw an exception if user validation fails or access is denied.
        throw new InternalErrorException();
    }

    /**
     * Checks if the user is valid and has the necessary permissions.
     *
     * @param username The username of the user requesting the action.
     * @param jwtToken The JWT token for authentication.
     * @return True if the user is valid and allowed, false otherwise.
     * @throws AccessDeniedException if the user does not have the necessary permissions.
     */
    public boolean isUserValidAndAllowed(String username, String jwtToken) {
        UserEntity userEntity = validateData.validateUserWithJWT(username, jwtToken);

        // Check if the user is an admin; if so, allow the action.
        if (validateData.isUserAdmin(userEntity.getRoles())) {
            return true;
        }

        // Throw an exception if the user is not allowed.
        throw new AccessDeniedException();
    }

}