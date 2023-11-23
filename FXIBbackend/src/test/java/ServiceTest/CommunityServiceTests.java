package ServiceTest;

import fxibBackend.dto.CommunityDTOS.AnswerDTO;
import fxibBackend.dto.CommunityDTOS.CommunityAgreeToTermsAndConditionsDTO;
import fxibBackend.dto.CommunityDTOS.QuestionDTO;
import fxibBackend.dto.CommunityDTOS.TopicDTO;
import fxibBackend.entity.*;
import fxibBackend.entity.enums.TopicEnum;
import fxibBackend.exception.AccessDeniedException;
import fxibBackend.exception.DataValidationException;
import fxibBackend.exception.ResourceNotFoundException;
import fxibBackend.repository.*;
import fxibBackend.service.CommunityService;
import fxibBackend.util.CustomDateFormatter;
import fxibBackend.util.ValidateData;
import fxibBackend.util.ValidationUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static fxibBackend.constants.ResponseConst.ANSWER_DELETED_SUCCESSFULLY;
import static fxibBackend.constants.ResponseConst.QUESTION_DELETED_SUCCESSFULLY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CommunityServiceTests {

    @InjectMocks
    private CommunityService communityService;
    @Mock
    private UserEntityRepository userRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private QuestionEntityRepository questionRepository;
    @Mock
    private AnswerEntityRepository answerRepository;
    @Mock
    private AnswerLikeEntityRepository answerLikeEntityRepository;
    @Mock
    private ValidateData validateData;
    @Mock
    private ValidationUtil validationUtil;
    @Mock
    private TopicEntityRepository topicEntityRepository;
    @Mock
    private CustomDateFormatter customDateFormatter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetTermsAndConditions() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setAgreedToTerms(false);

        when(validateData.validateUserWithJWT("testUser", "jwtToken")).thenReturn(userEntity);
        when(validateData.isUserBanned(userEntity.getRoles())).thenReturn(false);

        CommunityAgreeToTermsAndConditionsDTO expectedDTO = new CommunityAgreeToTermsAndConditionsDTO();
        when(modelMapper.map(userEntity, CommunityAgreeToTermsAndConditionsDTO.class)).thenReturn(expectedDTO);

        when(validationUtil.isValid(expectedDTO)).thenReturn(true);

        CommunityAgreeToTermsAndConditionsDTO result = communityService.setTermsAndConditions("testUser", true, "jwtToken");

        verify(userRepository).save(userEntity);

        Assert.assertEquals(expectedDTO, result);
    }

    @Test(expected = AccessDeniedException.class)
    public void testSetTermsAndConditionsAccessDenied() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setAgreedToTerms(true);

        when(validateData.validateUserWithJWT("testUser", "jwtToken")).thenReturn(userEntity);

        communityService.setTermsAndConditions("testUser", true, "jwtToken");
    }

    @Test(expected = DataValidationException.class)
    public void testSetTermsAndConditionsDataValidationException() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setAgreedToTerms(false);

        when(validateData.validateUserWithJWT("testUser", "jwtToken")).thenReturn(userEntity);
        when(validateData.isUserBanned(userEntity.getRoles())).thenReturn(false);

        CommunityAgreeToTermsAndConditionsDTO expectedDTO = new CommunityAgreeToTermsAndConditionsDTO();
        when(modelMapper.map(userEntity, CommunityAgreeToTermsAndConditionsDTO.class)).thenReturn(expectedDTO);

        when(validationUtil.isValid(expectedDTO)).thenReturn(false);

        communityService.setTermsAndConditions("testUser", true, "jwtToken");
    }

    @Test
    public void testGetTermsAndConditions() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");

        when(validateData.validateUserWithJWT("testUser", "jwtToken")).thenReturn(userEntity);

        CommunityAgreeToTermsAndConditionsDTO expectedDTO = new CommunityAgreeToTermsAndConditionsDTO();
        when(modelMapper.map(userEntity, CommunityAgreeToTermsAndConditionsDTO.class)).thenReturn(expectedDTO);

        when(validationUtil.isValid(expectedDTO)).thenReturn(true);

        CommunityAgreeToTermsAndConditionsDTO result = communityService.getTermsAndConditions("testUser", "jwtToken");

        Assert.assertEquals(expectedDTO, result);
    }

    @Test(expected = DataValidationException.class)
    public void testGetTermsAndConditionsDataValidationException() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");

        when(validateData.validateUserWithJWT("testUser", "jwtToken")).thenReturn(userEntity);

        CommunityAgreeToTermsAndConditionsDTO expectedDTO = new CommunityAgreeToTermsAndConditionsDTO();
        when(modelMapper.map(userEntity, CommunityAgreeToTermsAndConditionsDTO.class)).thenReturn(expectedDTO);

        when(validationUtil.isValid(expectedDTO)).thenReturn(false);

        communityService.getTermsAndConditions("testUser", "jwtToken");
    }

    @Test(expected = AccessDeniedException.class)
    public void testGetAllQuestionsWithInvalidUser() {
        String topic = "yourTopic";
        String username = "invalidUser";
        String jwtToken = "invalidToken";

        when(validateData.validateUserWithJWT(username, jwtToken.substring(7))).thenThrow(AccessDeniedException.class);

        communityService.getAllQuestions(topic, username, jwtToken);
    }

    @Test
    public void testGetAllQuestions_ResourceNotFound() {
        String topic = "NonExistentTopic";
        String username = "sampleUser";
        String jwtToken = "sampleToken";
        UserEntity userEntity = new UserEntity();

        when(validateData.validateUserWithJWT(username, jwtToken.substring(7)))
                .thenReturn(userEntity);

        assertThrows(ResourceNotFoundException.class, () -> communityService.getAllQuestions(topic, username, jwtToken));
    }

    @Test
    public void testGetUserEntityByUsername() {
        String username = "SampleUser";
        UserEntity userEntity = new UserEntity();

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        UserEntity result = communityService.getUserEntityByUsername(username);

        assertEquals(userEntity, result);
    }

    @Test
    public void testGetUserEntityByUsernameWhenUserNotFound() {
        String username = "NonExistentUser";

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> communityService.getUserEntityByUsername(username));
    }

    @Test
    public void testAddQuestion() {
        UserEntity userEntity = new UserEntity();
        userEntity.setAgreedToTerms(true);
        userEntity.setQuestions(new ArrayList<>());
        TopicEntity topicEntity = new TopicEntity();

        when(validateData.validateUserWithJWT(anyString(), anyString())).thenReturn(userEntity);
        when(validateData.isUserBanned(anySet())).thenReturn(false);
        when(topicEntityRepository.findTopicEntityByTopicEnum(any())).thenReturn(topicEntity);
        when(validationUtil.isValid(any())).thenReturn(true);

        Mockito.when(modelMapper.map(any(QuestionEntity.class), eq(QuestionDTO.class))).thenReturn(createMockQuestionDTO());

        QuestionDTO questionDTO = communityService.addQuestion("username", "Question content", "Forex Basics", "jwtToken");

        assertNotNull(questionDTO);
        assertEquals("MockedWriter", questionDTO.getWriter());
        assertFalse(questionDTO.getSolved());

        verify(userRepository, times(1)).save(eq(userEntity));
        verify(questionRepository, times(1)).save(any(QuestionEntity.class));
    }

    private QuestionDTO createMockQuestionDTO() {
        QuestionDTO questionDTOMock = new QuestionDTO();
        questionDTOMock.setWriter("MockedWriter");
        questionDTOMock.setSolved(false);
        return questionDTOMock;
    }

    @Test
    public void testAddAnswerToTheQuestion() {
        UserEntity userEntity = new UserEntity();
        userEntity.setAgreedToTerms(true);
        userEntity.setSubscription("Free");
        userEntity.setBiography("User Bio");

        when(validateData.validateUserWithJWT(anyString(), anyString())).thenReturn(userEntity);
        when(validateData.isUserBanned(anySet())).thenReturn(false);

        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setAnswers(new ArrayList<>());
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(questionEntity));

        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setDeleted(false);
        when(answerRepository.save(any(AnswerEntity.class))).thenReturn(answerEntity);

        AnswerDTO answerDTOMock = Mockito.mock(AnswerDTO.class);
        when(modelMapper.map(any(AnswerEntity.class), eq(AnswerDTO.class))).thenReturn(answerDTOMock);

        when(validationUtil.isValid(answerDTOMock)).thenReturn(true);
        when(validationUtil.isValid(questionEntity)).thenReturn(true);
        when(validationUtil.isValid(answerEntity)).thenReturn(true);

        AnswerDTO answerDTO = communityService.addAnswerToTheQuestion("username", "Answer content", 1L, "jwtToken");

        assertNotNull(answerDTO);
        assertNull(answerDTO.getWriter());
        assertEquals(0L, answerDTO.getVoteCount());
        assertNull(answerDTO.getUserSubscriptionPlan());
        assertNull(answerDTO.getUserBiography());

        verify(answerRepository, times(1)).save(any(AnswerEntity.class));
        verify(questionRepository, times(1)).save(questionEntity);
    }

    @Test
    public void testIncreaseAnswerVoteCount() {
        UserEntity userEntity = new UserEntity();
        userEntity.setAgreedToTerms(true);
        userEntity.setSubscription("Free");
        userEntity.setBiography("User Bio");

        when(validateData.validateUserWithJWT(anyString(), anyString())).thenReturn(userEntity);
        when(validateData.isUserBanned(anySet())).thenReturn(false);

        AnswerEntity answerEntity = new AnswerEntity();
        when(answerRepository.findByIdAndDeleted(anyLong(), anyBoolean())).thenReturn(Optional.of(answerEntity));

        AnswerLikeEntity answerLikeEntity = new AnswerLikeEntity();
        when(answerLikeEntityRepository.save(any(AnswerLikeEntity.class))).thenReturn(answerLikeEntity);

        AnswerDTO answerDTO = communityService.increaseAnswerVoteCount("username", 1L, "jwtToken");

        assertNull(answerDTO);

        verify(answerLikeEntityRepository, times(1)).save(any(AnswerLikeEntity.class));
        verify(userRepository, times(1)).save(userEntity);
        verify(answerRepository, times(1)).save(answerEntity);
    }

    @Test
    public void testDecreaseAnswerVoteCount() {
        UserEntity userEntity = new UserEntity();
        userEntity.setAgreedToTerms(true);
        userEntity.setSubscription("Free");
        userEntity.setBiography("User Bio");

        when(validateData.validateUserWithJWT(anyString(), anyString())).thenReturn(userEntity);
        when(validateData.isUserBanned(anySet())).thenReturn(false);

        AnswerEntity answerEntity = new AnswerEntity();
        when(answerRepository.findByIdAndDeleted(anyLong(), anyBoolean())).thenReturn(Optional.of(answerEntity));

        AnswerLikeEntity answerLikeEntity = new AnswerLikeEntity();
        when(answerLikeEntityRepository.getAnswerLikeEntityByAnswerIDAndUsernameAndDeleted(anyLong(), anyString(), anyBoolean()))
                .thenReturn(Optional.of(answerLikeEntity));

        AnswerDTO answerDTO = communityService.decreaseAnswerVoteCount("username", 1L, "jwtToken");

        assertNull(answerDTO);

        verify(answerRepository, times(1)).save(answerEntity);
        verify(answerLikeEntityRepository, times(1)).delete(answerLikeEntity);
    }

    @Test
    public void testSetQuestionSolved() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("username");
        userEntity.setAgreedToTerms(true);

        when(validateData.validateUserWithJWT(anyString(), anyString())).thenReturn(userEntity);
        when(validateData.isUserBanned(anySet())).thenReturn(false);

        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setWriter("username");
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(questionEntity));

        QuestionDTO questionDTO = communityService.setQuestionSolved("username", 1L, "jwtToken");

        assertNull(questionDTO);

        verify(questionRepository, times(1)).save(questionEntity);
    }

    @Test
    public void testGetAllTopicsText() {
        List<TopicEntity> topicEntities = new ArrayList<>();
        TopicEntity topicEntity1 = new TopicEntity();
        topicEntity1.setTopicEnum(TopicEnum.FOREX_BASICS);
        topicEntities.add(topicEntity1);

        when(topicEntityRepository.findAllByOrderByIdAsc()).thenReturn(topicEntities);
        when(validationUtil.isValid(any(TopicDTO.class))).thenReturn(true);

        List<String> topicTexts = communityService.getAllTopicsText();

        assertNotNull(topicTexts);
        assertEquals(1, topicTexts.size());
        assertEquals("Forex Basics", topicTexts.get(0));
    }

    @Test
    public void testIsUserValidAndAllowed() {
        UserEntity userEntity = new UserEntity();
        when(validateData.validateUserWithJWT(anyString(), anyString())).thenReturn(userEntity);
        when(validateData.isUserAdmin(anySet())).thenReturn(true);

        assertThrows(AccessDeniedException.class, () -> communityService.isUserValidAndAllowed("username", "jwtToken"));
    }

    @Test
    public void testDeleteQuestion() {
        UserEntity userEntity = new UserEntity();
        userEntity.setRoles(new HashSet<>());
        when(validateData.validateUserWithJWT(anyString(), anyString())).thenReturn(userEntity);

        QuestionEntity question = new QuestionEntity();
        question.setAnswers(new ArrayList<>());
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question));
        when(answerLikeEntityRepository.findByAnswerID(anyLong())).thenReturn(Optional.of(new AnswerLikeEntity()));
        when(validateData.isUserAdmin(userEntity.getRoles())).thenReturn(true);

        when(communityService.isUserValidAndAllowed(anyString(), anyString())).thenReturn(true);

        String result = communityService.deleteQuestion("username", "jwtToken", 1L);

        assertEquals(QUESTION_DELETED_SUCCESSFULLY, result);

        verify(questionRepository, times(1)).save(any(QuestionEntity.class));
    }

    @Test
    public void testDeleteAnswer_WhenUserIsValidAndAnswerExists() {
        String username = "testUser";
        String jwtToken = "testToken";
        Long answerID = 1L;

        UserEntity userEntity = new UserEntity();
        userEntity.setRoles(new HashSet<>());
        when(validateData.validateUserWithJWT(username, jwtToken)).thenReturn(userEntity);

        when(validateData.isUserAdmin(userEntity.getRoles())).thenReturn(true);
        when(communityService.isUserValidAndAllowed(username, jwtToken)).thenReturn(true);
        when(answerRepository.findById(answerID)).thenReturn(Optional.empty());

        AnswerEntity answerEntity = new AnswerEntity();
        when(answerRepository.findById(answerID)).thenReturn(Optional.of(answerEntity));

        String result = communityService.deleteAnswer(username, jwtToken, answerID);

        verify(answerRepository).save(answerEntity);

        verify(answerLikeEntityRepository, never()).getByAnswerID(anyLong());
        verify(answerLikeEntityRepository, never()).save(any());

        assertEquals(ANSWER_DELETED_SUCCESSFULLY, result);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteAnswer_WhenUserIsValidAndAnswerDoesNotExist() {
        String username = "testUser";
        String jwtToken = "testToken";
        Long answerID = 1L;
        UserEntity userEntity = new UserEntity();
        userEntity.setRoles(new HashSet<>());

        when(validateData.validateUserWithJWT(username, jwtToken)).thenReturn(userEntity);

        when(validateData.isUserAdmin(userEntity.getRoles())).thenReturn(true);
        when(communityService.isUserValidAndAllowed(username, jwtToken)).thenReturn(true);
        when(answerRepository.findById(answerID)).thenReturn(Optional.empty());

        communityService.deleteAnswer(username, jwtToken, answerID);

        verify(answerRepository, never()).save(any());
    }

}
