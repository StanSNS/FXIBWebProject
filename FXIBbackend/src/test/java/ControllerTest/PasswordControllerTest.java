package ControllerTest;

import fxibBackend.controller.PasswordController;
import fxibBackend.service.EmailService;
import fxibBackend.service.UserDetailsService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static fxibBackend.constants.ResponseConst.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PasswordControllerTest {

    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private EmailService emailService;
    private PasswordController passwordController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        passwordController = new PasswordController(userDetailsService, emailService);
    }

    @Test
    void testChangeUserPassword() {
        when(userDetailsService.resetUserPassword(any(), any(), any(), any())).thenReturn(PASSWORD_MATCHES);

        ResponseEntity<String> response = passwordController.changeUserPassword("testUser", "token", "oldPassword", "newPassword");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testResetPassword() throws MessagingException {
        when(emailService.sendResetPasswordEmail(any())).thenReturn(PASSWORD_CHANGE_EMAIL_SENT_SUCCESSFULLY);

        ResponseEntity<String> response = passwordController.resetPassword("test@example.com");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(PASSWORD_CHANGE_EMAIL_SENT_SUCCESSFULLY, response.getBody());
    }

    @Test
    void testResetPasswordUpdateSuccess() {
        when(userDetailsService.resetPasswordByToken(any(), any())).thenReturn(PASSWORD_RESET_SUCCESSFULLY);

        ResponseEntity<String> response = passwordController.resetPasswordUpdate("resetToken", "newPassword");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(PASSWORD_RESET_SUCCESSFULLY, response.getBody());
    }

    @Test
    void testResetPasswordUpdateFailure() {
        when(userDetailsService.resetPasswordByToken(any(), any())).thenReturn("Fail");

        ResponseEntity<String> response = passwordController.resetPasswordUpdate("resetToken", "newPassword");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testResetPasswordEmailSendFailed() throws MessagingException {
        when(emailService.sendResetPasswordEmail(any())).thenReturn("Fail");

        ResponseEntity<String> response = passwordController.resetPassword("test@example.com");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
