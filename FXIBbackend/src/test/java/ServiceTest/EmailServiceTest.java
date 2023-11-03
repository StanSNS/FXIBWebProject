package ServiceTest;

import fxibBackend.dto.AuthorizationDTOS.LoginDTO;
import fxibBackend.dto.UserDetailsDTO.LocationDTO;
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

}
