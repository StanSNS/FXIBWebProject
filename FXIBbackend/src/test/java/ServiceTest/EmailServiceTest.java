package ServiceTest;

import fxibBackend.dto.AuthorizationDTOS.LoginDTO;
import fxibBackend.dto.UserDetailsDTO.LocationDTO;
import fxibBackend.entity.InquiryEntity;
import fxibBackend.entity.TransactionEntity;
import fxibBackend.entity.UserEntity;
import fxibBackend.exception.ResourceNotFoundException;
import fxibBackend.repository.UserEntityRepository;
import fxibBackend.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Optional;

import static fxibBackend.constants.ResponseConst.PASSWORD_CHANGE_EMAIL_SENT_SUCCESSFULLY;
import static fxibBackend.constants.ResponseConst.TWO_FACTOR_CODE_EMAIL_SENT_SUCCESSFULLY;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EmailServiceTest {

    @Mock
    private MimeMessage mimeMessage;
    @Mock
    private UserEntityRepository userEntityRepository;
    @Mock
    private JavaMailSender javaMailSender;
    private EmailService emailService;

    @Before
    public void setUp() {
        userEntityRepository = Mockito.mock(UserEntityRepository.class);
        javaMailSender = Mockito.mock(JavaMailSender.class);
        mimeMessage = Mockito.mock(MimeMessage.class);
        emailService = new EmailService(userEntityRepository, javaMailSender);
    }

    @Test
    public void testSendResetPasswordEmail() throws Exception {
        String toEmail = "test@example.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(toEmail);
        when(userEntityRepository.findByEmail(toEmail)).thenReturn(Optional.of(userEntity));
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        String result = emailService.sendResetPasswordEmail(toEmail);

        assertEquals("Expected result", PASSWORD_CHANGE_EMAIL_SENT_SUCCESSFULLY, result);
    }

    @Test
    public void testSend2FactorAuthEmail() throws MessagingException {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testUser");
        String toEmail = "test@example.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(toEmail);
        when(userEntityRepository.findByUsername(loginDTO.getUsername())).thenReturn(Optional.of(userEntity));
        when(userEntityRepository.existsByTwoFactorLoginCode(Mockito.anyInt())).thenReturn(false);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        String result = emailService.send2FactorAuthEmail(loginDTO);

        assertEquals("Expected result", TWO_FACTOR_CODE_EMAIL_SENT_SUCCESSFULLY, result);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testSendResetPasswordEmail_ResourceNotFoundException() throws MessagingException {
        String toEmail = "test@example.com";
        when(userEntityRepository.findByEmail(toEmail)).thenReturn(Optional.empty());

        emailService.sendResetPasswordEmail(toEmail);
    }

    @Test
    public void testSendLocationDifferenceEmail() throws MessagingException {
        LocationDTO originalLocationDTO = new LocationDTO();
        LocationDTO currentLocationDTO = new LocationDTO();

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("user@example.com");

        when(userEntityRepository.findByUsername(originalLocationDTO.getUsername())).thenReturn(Optional.of(userEntity));
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendLocationDifferenceEmail(originalLocationDTO, currentLocationDTO);

        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }


    @Test
    public void testSendSuccessfulRegistrationEmail() throws MessagingException {
        String username = "testUser";
        String email = "test@example.com";

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        emailService.sendSuccessfulRegistrationEmail(username, email);

        verify(javaMailSender, times(1)).send(any(MimeMessage.class));

    }

    @Test
    public void testSendSuccessfulPaymentEmail() throws MessagingException {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setUserEmail("Test");
        String username = "testUser";

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        emailService.sendSuccessfulPaymentEmail(transactionEntity, username);

        verify(javaMailSender, times(1)).send(any(MimeMessage.class));

    }


    @Test
    public void testSendInquiryEmail() throws MessagingException {
        EmailService emailService = new EmailService(userEntityRepository, javaMailSender);

        InquiryEntity inquiryEntity = new InquiryEntity();
        inquiryEntity.setTitle("Test Inquiry");
        inquiryEntity.setContent("This is a test inquiry");
        inquiryEntity.setDate("2023-11-22");
        inquiryEntity.setCustomID("123");
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("user@example.com");
        inquiryEntity.setUserEntity(userEntity);

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendInquiryEmail(inquiryEntity);

        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    public void testSendBanUserEmail() throws MessagingException {
        EmailService emailService = new EmailService(userEntityRepository, javaMailSender);

        UserEntity bannedUser = new UserEntity();
        bannedUser.setUsername("bannedUser");
        bannedUser.setEmail("banned@example.com");

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendBanUserEmail(bannedUser);

        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    public void testSendUnbanUserEmail() throws MessagingException {
        EmailService emailService = new EmailService(userEntityRepository, javaMailSender);

        UserEntity unbannedUser = new UserEntity();
        unbannedUser.setUsername("unbannedUser");
        unbannedUser.setEmail("unbanned@example.com");

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendUnbanUserEmail(unbannedUser);

        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }


}
