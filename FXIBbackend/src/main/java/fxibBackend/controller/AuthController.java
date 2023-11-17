package fxibBackend.controller;

import fxibBackend.dto.AuthorizationDTOS.LoginDTO;
import fxibBackend.dto.AuthorizationDTOS.RegisterDTO;
import fxibBackend.dto.AuthorizationDTOS.UserRegisterUsernameAndEmailDTO;
import fxibBackend.service.AuthService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static fxibBackend.constants.MappingConstants.*;
import static fxibBackend.constants.URLAccessConst.FRONTEND_BASE_URL;

@CrossOrigin(FRONTEND_BASE_URL)
@RequiredArgsConstructor
@RestController
@RequestMapping
public class AuthController {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final AuthService authService;

    /**
     * Registers a new user based on the provided registration information.
     *
     * @param registerDto The registration data of the user.
     * @return A ResponseEntity with a success message if registration is successful, or an error response if registration fails.
     */
    @PostMapping(AUTH_CONTROLLER_MAPPING_REGISTER)
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDto) throws MessagingException {
        // Attempt to register a new user with the provided data
        return new ResponseEntity<>(authService.register(registerDto), HttpStatus.CREATED);
    }

    /**
     * Retrieves a list of user usernames and email addresses for registration.
     *
     * @return A ResponseEntity containing a list of UserRegisterUsernameAndEmailDTO objects with usernames and emails.
     */
    @GetMapping(AUTH_CONTROLLER_MAPPING_REGISTER)
    public ResponseEntity<List<UserRegisterUsernameAndEmailDTO>> getAllUserUsernameAndEmails() {
        // Retrieve a list of usernames and emails for registration
        return new ResponseEntity<>(authService.getAllUsernamesAndEmails(), HttpStatus.OK);
    }

    /**
     * Handles the user login process, including 2-factor authentication if required.
     *
     * @param loginDto The login data of the user.
     * @return A ResponseEntity with a success message or redirection to 2FA login, or an error response if login fails.
     * @throws MessagingException if there's an issue with sending 2-factor authentication emails.
     */
    @PostMapping(AUTH_CONTROLLER_MAPPING_LOGIN)
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDto) throws MessagingException {
        // Check if 2-factor authentication is required and send the appropriate response
        if (authService.isReadyFor2FactorAuth(loginDto)) {
            authService.sendTwoFactorAuthEmail(loginDto);
            return new ResponseEntity<>("Redirect to 2FA Login", HttpStatus.ACCEPTED);
        }

        // Handle regular login and return an appropriate response
        return new ResponseEntity<>(authService.login(loginDto), HttpStatus.OK);
    }

    /**
     * Handles the 2-factor authentication login process.
     *
     * @param username The username of the user.
     * @param password The user's password.
     * @param code     The 2-factor authentication code.
     * @return A ResponseEntity with a success message or an error response if 2-factor authentication fails.
     * @throws MessagingException if there's an issue with 2-factor authentication.
     */
    @PostMapping(TWO_FACTOR_AUTH_CONTROLLER_MAPPING_LOGIN)
    public ResponseEntity<?> login2FA(@RequestParam String username, @RequestParam String password, @RequestParam String code) throws MessagingException {
        // Handle 2-factor authentication login and return an appropriate response
        return new ResponseEntity<>(authService.twoFactorAuthLogin(new LoginDTO(username, password), code), HttpStatus.OK);
    }


}