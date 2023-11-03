package fxibBackend.controller;

import fxibBackend.service.EmailService;
import fxibBackend.service.UserDetailsService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static fxibBackend.constants.MappingConstants.*;
import static fxibBackend.constants.ResponseConst.*;
import static fxibBackend.constants.URLAccessConst.FRONTEND_BASE_URL;


@CrossOrigin(FRONTEND_BASE_URL)
@RequiredArgsConstructor
@RestController
@RequestMapping
public class PasswordController {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final UserDetailsService userDetailsService;
    private final EmailService emailService;

    /**
     * Changes the password of a user after successful authentication.
     *
     * @param username    The username of the user.
     * @param jwtToken    JWT token for authentication.
     * @param oldPassword The user's old password.
     * @param newPassword The user's new password.
     * @return A ResponseEntity with a success message if password change is successful, or an error response if the operation fails.
     */
    @PreAuthorize("hasAnyRole('ADMIN','USER','BANNED')")
    @PutMapping(PASSWORD_CONTROLLER_MAPPING_CHANGE)
    public ResponseEntity<String> changeUserPassword(@RequestParam("username") String username,
                                                     @RequestParam("jwtToken") String jwtToken,
                                                     @RequestParam("oldPassword") String oldPassword,
                                                     @RequestParam("newPassword") String newPassword) {
        // Attempt to change the user's password after authentication
        return new ResponseEntity<>(userDetailsService.resetUserPassword(username, oldPassword, newPassword, jwtToken), HttpStatus.OK);
    }

    /**
     * Sends a reset password email to the provided email address.
     *
     * @param toEmail The email address to which the reset password email will be sent.
     * @return A ResponseEntity with a success message if the email is sent successfully, or an error response if sending the email fails.
     * @throws MessagingException if there's an issue with sending the email.
     */
    @PostMapping(PASSWORD_CONTROLLER_MAPPING_RESET)
    public ResponseEntity<String> resetPassword(@RequestParam String toEmail) throws MessagingException {
        // Send a reset password email and handle the response
        String responseEmailSend = emailService.sendResetPasswordEmail(toEmail);
        if (responseEmailSend.equals(PASSWORD_CHANGE_EMAIL_SENT_SUCCESSFULLY)) {
            return new ResponseEntity<>(responseEmailSend, HttpStatus.OK);
        }
        return new ResponseEntity<>(responseEmailSend, HttpStatus.BAD_REQUEST);
    }

    /**
     * Updates the user's password using a reset token.
     *
     * @param resetToken  The reset token provided to the user.
     * @param newPassword The user's new password.
     * @return A ResponseEntity with a success message if password reset is successful, or an error response if the operation fails.
     */
    @PutMapping(PASSWORD_CONTROLLER_MAPPING_UPDATE)
    public ResponseEntity<String> resetPasswordUpdate(@RequestParam("resetToken") String resetToken,
                                                      @RequestParam("newPassword") String newPassword) {
        // Attempt to reset the user's password using the provided reset token
        String responseMessage = userDetailsService.resetPasswordByToken(resetToken, newPassword);
        if (responseMessage.equals(PASSWORD_RESET_SUCCESSFULLY)) {
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        }
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

}
