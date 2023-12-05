package ServiceTest;

import com.stripe.exception.StripeException;
import fxibBackend.dto.UserDetailsDTO.StripeTransactionDTO;
import fxibBackend.dto.UserDetailsDTO.UserDetailsDTO;
import fxibBackend.entity.InquiryEntity;
import fxibBackend.entity.ReportEntity;
import fxibBackend.entity.TransactionEntity;
import fxibBackend.entity.UserEntity;
import fxibBackend.exception.AccessDeniedException;
import fxibBackend.exception.DataValidationException;
import fxibBackend.exception.ResourceNotFoundException;
import fxibBackend.repository.InquiryEntityRepository;
import fxibBackend.repository.ReportEntityRepository;
import fxibBackend.repository.TransactionEntityRepository;
import fxibBackend.repository.UserEntityRepository;
import fxibBackend.service.EmailService;
import fxibBackend.service.StripeService;
import fxibBackend.service.UserDetailsService;
import fxibBackend.util.CustomDateFormatter;
import fxibBackend.util.ValidateData;
import fxibBackend.util.ValidationUtil;
import jakarta.mail.MessagingException;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fxibBackend.constants.OtherConst.*;
import static fxibBackend.constants.ResponseConst.SUCCESSFUL_LOGOUT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserDetailsServiceTest {

    private UserDetailsService userDetailsService;
    @Mock
    private UserEntityRepository userRepository;
    @Mock
    private StripeService stripeService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ValidateData validateData;
    @Mock
    private ValidationUtil validationUtil;
    @Mock
    private EmailService emailService;
    @Mock
    private InquiryEntityRepository inquiryEntityRepository;

    @Mock
    private ReportEntityRepository reportEntityRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private TransactionEntityRepository transactionEntityRepository;
    @Mock
    private CustomDateFormatter customDateFormatter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userDetailsService = new UserDetailsService(
                userRepository,
                stripeService,
                passwordEncoder,
                validateData,
                validationUtil,
                modelMapper,
                transactionEntityRepository,
                emailService,
                inquiryEntityRepository,
                customDateFormatter,
                reportEntityRepository
        );
    }

    @Test
    public void testGetUserDetailsDTO() throws StripeException {
        String username = "testUser";
        String jwtToken = "testToken";
        UserEntity userEntity = new UserEntity();
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO();

        when(validationUtil.isValid(userEntity)).thenReturn(false);
        when(validationUtil.isValid(userDetailsDTO)).thenReturn(true);

        when(validateData.validateUserWithJWT(username, jwtToken)).thenReturn(userEntity);
        when(modelMapper.map(eq(userEntity), eq(UserDetailsDTO.class))).thenReturn(userDetailsDTO);
        when(stripeService.getAllTransactionsFromEmail(userEntity.getEmail())).thenReturn(new ArrayList<>());

        assertThrows(DataValidationException.class, () -> userDetailsService.getUserDetailsDTO(username, jwtToken));

        verify(validationUtil).isValid(userEntity);
        verify(validationUtil, never()).isValid(userDetailsDTO);
    }

    @Test
    public void testGetUpdateUserBiographyDTOValidationFailure() {
        String username = "testUser";
        String jwtToken = "testToken";
        String biography = "Test biography";
        UserEntity userEntity = new UserEntity();

        when(validateData.validateUserWithJWT(username, jwtToken)).thenReturn(userEntity);
        when(validationUtil.isValid(any(UserEntity.class))).thenReturn(false);

        assertThrows(DataValidationException.class, () -> userDetailsService.getUpdateUserBiographyDTO(username, jwtToken, biography));

        ArgumentCaptor<UserEntity> userEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(validationUtil).isValid(userEntityCaptor.capture());

    }

    @Test
    public void testGetAllUserTransactions() throws StripeException {
        String username = "testUser";
        String jwtToken = "testToken";
        UserEntity userEntity = new UserEntity();
        List<StripeTransactionDTO> transactionsFromEmailDTOs = new ArrayList<>();
        List<TransactionEntity> allByUserEmail = new ArrayList<>();

        when(validateData.validateUserWithJWT(username, jwtToken)).thenReturn(userEntity);
        when(stripeService.getAllTransactionsFromEmail(userEntity.getEmail())).thenReturn(transactionsFromEmailDTOs);
        when(transactionEntityRepository.findAllByUserEmail(userEntity.getEmail())).thenReturn(allByUserEmail);

        verify(transactionEntityRepository, times(0)).saveAll(anyList());
    }

    @Test
    void testGetAllUserTransactions2() throws StripeException {
        String username = "testUser";
        String jwtToken = "testToken";
        UserEntity userEntity = new UserEntity();

        when(validateData.validateUserWithJWT(username, jwtToken)).thenReturn(userEntity);

        List<StripeTransactionDTO> stripeTransactionsFromService = new ArrayList<>();
        StripeTransactionDTO stripeTransactionDTO1 = new StripeTransactionDTO();
        stripeTransactionDTO1.setUserEmail("user@example.com");
        stripeTransactionDTO1.setBillingDate("2023-11-21");
        stripeTransactionDTO1.setDuration("1 Month");
        stripeTransactionDTO1.setEndOfBillingDate("2023-12-21");
        stripeTransactionDTO1.setAmount("$100");
        stripeTransactionDTO1.setCard("**** **** **** 1234");
        stripeTransactionDTO1.setStatus("Success");
        stripeTransactionDTO1.setReceipt("receipt123");
        stripeTransactionDTO1.setDescription("Payment for services");
        stripeTransactionDTO1.setEmailSent(false);
        stripeTransactionsFromService.add(stripeTransactionDTO1);

        when(stripeService.getAllTransactionsFromEmail(userEntity.getEmail())).thenReturn(stripeTransactionsFromService);

        List<TransactionEntity> transactionsFromRepository = new ArrayList<>();
        TransactionEntity transactionEntity1 = new TransactionEntity();
        transactionEntity1.setUserEmail("user@example.com");
        transactionEntity1.setBillingDate("2023-11-21");
        transactionEntity1.setDuration("1 Month");
        transactionEntity1.setEndOfBillingDate("2023-12-21");
        transactionEntity1.setAmount("$100");
        transactionEntity1.setCard("**** **** **** 1234");
        transactionEntity1.setStatus("Success");
        transactionEntity1.setReceipt("receipt123");
        transactionEntity1.setDescription("Payment for services");
        transactionEntity1.setEmailSent(false);
        transactionsFromRepository.add(transactionEntity1);

        when(transactionEntityRepository.findAllByUserEmail(userEntity.getEmail())).thenReturn(transactionsFromRepository);

        List<StripeTransactionDTO> result = userDetailsService.getAllUserTransactions(username, jwtToken);

        assertEquals(stripeTransactionsFromService.size(), transactionsFromRepository.size());
        assertEquals(stripeTransactionsFromService.size(), result.size());
    }


    @Test
    public void testLogoutUser() {
        String username = "testUser";
        String jwtToken = "testToken";
        UserEntity userEntity = new UserEntity();

        when(validateData.validateUserWithJWT(username, jwtToken)).thenReturn(userEntity);

        String result = userDetailsService.logoutUser(username, jwtToken);

        assertEquals(SUCCESSFUL_LOGOUT, result);

        verify(userRepository).save(userEntity);
    }

    @Test
    public void testResetPasswordByTokenInvalidToken() {
        String resetToken = "resetToken";
        String newPassword = "newPassword";
        String hashedToken = DigestUtils.sha256Hex(resetToken);

        when(userRepository.findUserEntityByResetToken(hashedToken)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userDetailsService.resetPasswordByToken(resetToken, newPassword));

        verifyNoInteractions(passwordEncoder, validationUtil);
    }

    @Test
    public void testResetPasswordByTokenMismatchedToken() {
        String resetToken = "resetToken";
        String newPassword = "newPassword";
        String hashedToken = DigestUtils.sha256Hex(resetToken);

        UserEntity userEntity = new UserEntity();
        userEntity.setResetToken("differentToken");

        when(userRepository.findUserEntityByResetToken(hashedToken)).thenReturn(Optional.of(userEntity));

        assertThrows(AccessDeniedException.class, () -> userDetailsService.resetPasswordByToken(resetToken, newPassword));

        verifyNoInteractions(passwordEncoder, validationUtil);
    }

    @Test
    public void testGenerateSubscriptionPlanName() {
        assertEquals(DURATION_LEVEL_ONE, userDetailsService.generateSubscriptionPlanName(PLAN_DURATION_ONE));
        assertEquals(DURATION_LEVEL_TWO, userDetailsService.generateSubscriptionPlanName(PLAN_DURATION_TWO));
        assertEquals(DURATION_LEVEL_THREE, userDetailsService.generateSubscriptionPlanName(PLAN_DURATION_THREE));
        assertEquals(DEFAULT_USER_SUBSCRIPTION, userDetailsService.generateSubscriptionPlanName("otherDuration"));
    }


    @Test
    void testResetUserPassword() {
        String username = "testuser";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        String jwtToken = "yourToken";

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(passwordEncoder.encode(oldPassword));

        when(validateData.validateUserWithJWT(username, jwtToken)).thenReturn(userEntity);
        when(passwordEncoder.matches(oldPassword, userEntity.getPassword())).thenReturn(true);
        when(validationUtil.isValid(userEntity)).thenReturn(true);

        String result = userDetailsService.resetUserPassword(username, oldPassword, newPassword, jwtToken);

        assertEquals("Passwords matches.", result);

        verify(userRepository, times(1)).save(userEntity);
    }


    @Test
    void sendInquiryEmailAndSave_Success() throws MessagingException {
        String title = "Test Title";
        String content = "Test Content";
        String username = "testUser";
        String jwtToken = "testToken";

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);

        InquiryEntity inquiryEntity = new InquiryEntity();
        inquiryEntity.setTitle(title);
        inquiryEntity.setContent(content);

        Mockito.when(validateData.validateUserWithJWT(username, jwtToken)).thenReturn(userEntity);
        Mockito.when(inquiryEntityRepository.existsByCustomID(Mockito.anyString())).thenReturn(false);

        userDetailsService.sendInquiryEmailAndSave(title, content, username, jwtToken);

        Mockito.verify(inquiryEntityRepository, Mockito.times(1)).save(Mockito.any(InquiryEntity.class));
        Mockito.verify(emailService, Mockito.times(1)).sendInquiryEmail(Mockito.any(InquiryEntity.class));
    }


    @Test
    void testGetRandomCustomIDNumberREP() {
        when(reportEntityRepository.existsByCustomID(any())).thenReturn(false);

        String result = ReflectionTestUtils.invokeMethod(userDetailsService, "getRandomCustomIDNumberREP");

        verify(reportEntityRepository, times(1)).existsByCustomID(any());

        assertNotNull(result);
        assertTrue(result.startsWith("REP"));
    }

    @Test
    void testSaveReportAndSendEmail() throws MessagingException {
        String title = "Test Title";
        String content = "Test Content";
        String imgURL = "https://example.com/image.jpg";
        String username = "testUser";
        String jwtToken = "testToken";

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);

        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setTitle(title);
        reportEntity.setContent(content);
        reportEntity.setDate(customDateFormatter.formatLocalDateTimeNowAsString(LocalDateTime.now()));
        reportEntity.setImgURL(imgURL);

        when(validateData.validateUserWithJWT(username, jwtToken)).thenReturn(userEntity);
        when(reportEntityRepository.save(Mockito.any(ReportEntity.class))).thenReturn(reportEntity);

        assertDoesNotThrow(() -> userDetailsService.saveReportAndSendEmail(title, content, imgURL, username, jwtToken));

        verify(reportEntityRepository, times(1)).save(Mockito.any(ReportEntity.class));
        verify(emailService, times(1)).sendReportEmail(Mockito.any(ReportEntity.class), eq(username), eq(userEntity.getEmail()));
    }

}
