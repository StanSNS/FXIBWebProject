package fxibBackend.service;

import fxibBackend.dto.AuthorizationDTOS.JwtAuthResponseDTO;
import fxibBackend.dto.AuthorizationDTOS.LoginDTO;
import fxibBackend.dto.AuthorizationDTOS.RegisterDTO;
import fxibBackend.dto.AuthorizationDTOS.UserRegisterUsernameAndEmailDTO;
import fxibBackend.entity.RoleEntity;
import fxibBackend.entity.UserEntity;
import fxibBackend.exception.*;
import fxibBackend.repository.RoleEntityRepository;
import fxibBackend.repository.UserEntityRepository;
import fxibBackend.security.JWT.JwtTokenProvider;
import fxibBackend.util.ValidationUtil;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static fxibBackend.constants.ConfigConst.MAX_LOGINS;
import static fxibBackend.constants.OtherConst.DEFAULT_USER_SUBSCRIPTION;

import static fxibBackend.constants.ResponseConst.TWO_FACTOR_CODE_EMAIL_SENT_SUCCESSFULLY;
import static fxibBackend.constants.ResponseConst.USER_REGISTER_SUCCESSFULLY;
import static fxibBackend.constants.RoleConst.ADMIN_C;
import static fxibBackend.constants.RoleConst.USER_C;

@Service
@RequiredArgsConstructor
public class AuthService {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final UserEntityRepository userRepository;
    private final RoleEntityRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final EmailService emailService;
    private final LocationService locationService;

    /**
     * Registers a new user with the provided RegisterDTO information.
     *
     * @param registerDto Registration data for the new user.
     * @return A success message if registration is successful.
     * @throws DataValidationException if the registration data is not valid.
     * @throws ResourceAlreadyExistsException if the username already exists.
     */
    public String register(RegisterDTO registerDto) {
        // Validate the registration data
        if (!validationUtil.isValid(registerDto)) {
            throw new DataValidationException();
        }

        // Check if the username already exists
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new ResourceAlreadyExistsException();
        }

        // Create a new UserEntity and set its properties
        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRegistrationDate(LocalDateTime.now());
        user.setSubscription(DEFAULT_USER_SUBSCRIPTION);
        user.setBiography("");
        user.setResetToken("");
        user.setAgreedToTerms(false);
        user.setNumberOfLogins(0);
        user.setLocationEntity(locationService.getTheInitialLocation(user.getUsername()));

        // Set user roles (assign ADMIN role to the first user if there are no users yet)
        Set<RoleEntity> roles = new HashSet<>();
        if (userRepository.count() == 0) {
            roles.add(roleRepository.findByName(ADMIN_C));
        }
        RoleEntity userRole = roleRepository.findByName(USER_C);
        roles.add(userRole);
        user.setRoles(roles);

        // Save the user entity to the repository
        userRepository.save(user);

        return USER_REGISTER_SUCCESSFULLY;
    }


    /**
     * Retrieves a list of usernames and email addresses of all registered users.
     *
     * @return A list of user data containing usernames and email addresses.
     * @throws DataValidationException if the retrieved data is not valid.
     */
    public List<UserRegisterUsernameAndEmailDTO> getAllUsernamesAndEmails() {
        return userRepository
                .findAll()
                .stream()
                .map(userEntity -> {
                    UserRegisterUsernameAndEmailDTO userRegisterUsernameAndEmailDTO = modelMapper.map(userEntity, UserRegisterUsernameAndEmailDTO.class);
                    if (!validationUtil.isValid(userRegisterUsernameAndEmailDTO)) {
                        throw new DataValidationException();
                    }
                    return userRegisterUsernameAndEmailDTO;
                })
                .collect(Collectors.toList());
    }


    /**
     * Logs in a user with the provided login credentials and generates a JWT token for authentication.
     *
     * @param loginDto Login credentials including username and password.
     * @return JwtAuthResponseDTO containing the JWT token and user role(s) on successful login.
     * @throws DataValidationException if the login data is not valid.
     * @throws InternalErrorException if an internal error occurs during login.
     */
    @Transactional
    public JwtAuthResponseDTO login(LoginDTO loginDto) throws MessagingException {
        // Validate the login data
        if (!validationUtil.isValid(loginDto)) {
            throw new DataValidationException();
        }
        UserEntity user = null;

        // Authenticate the user with provided credentials
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Retrieve the user entity by username
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(loginDto.getUsername());
        String token = "";
        JwtAuthResponseDTO jwtAuthResponse = new JwtAuthResponseDTO();

        if (userEntityOptional.isPresent()) {
            user = userEntityOptional.get();
            Set<RoleEntity> roles = user.getRoles();

            // Generate or retrieve the user's JWT token
            if (user.getJwtToken() == null || user.getJwtToken().isEmpty() || !jwtTokenProvider.validateToken(user.getJwtToken())) {
                token = jwtTokenProvider.generateToken(authentication);
                user.setJwtToken(token);
                userRepository.save(user);
            } else {
                token = user.getJwtToken();
            }
            if (!roles.isEmpty()) {
                jwtAuthResponse.setRole(roles.stream().map(RoleEntity::getName).collect(Collectors.toSet()));
            }
        }
        // Check if the token is empty and throw an error if so
        if (token.isEmpty()) {
            throw new InternalErrorException();
        }
        jwtAuthResponse.setAccessToken(token);
        jwtAuthResponse.setEmail(user.getEmail());
        // Validate the JWT response
        if (!validationUtil.isValid(jwtAuthResponse)) {
            throw new DataValidationException();
        }

        // Update user login count and location if necessary
        user.setNumberOfLogins(user.getNumberOfLogins() + 1);
        if (locationService.isLocationDifferent(user.getLocationEntity(), user.getUsername())) {
            user.setNumberOfLogins(MAX_LOGINS);
        }


        return jwtAuthResponse;
    }


    /**
     * Logs in a user with two-factor authentication using the provided login credentials and verification code.
     *
     * @param loginDto The login credentials, including the username and password.
     * @param code The two-factor authentication verification code.
     * @return JwtAuthResponseDTO containing the JWT token and user role(s) on successful two-factor authentication login.
     * @throws MessagingException if there is an issue with messaging during two-factor authentication.
     * @throws AccessDeniedException if the verification code does not match, denying access.
     */
    public JwtAuthResponseDTO twoFactorAuthLogin(LoginDTO loginDto, String code) throws MessagingException {
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(loginDto.getUsername());

        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();

            // Check if the provided code matches the user's two-factor authentication code
            if (userEntity.getTwoFactorLoginCode().toString().equals(code)) {
                userEntity.setNumberOfLogins(0);
                userEntity.setTwoFactorLoginCode(0);
                userRepository.save(userEntity);

                // Proceed with the standard login process after successful two-factor authentication
                return login(loginDto);
            }
        }
        // Deny access if the verification code is incorrect
        throw new AccessDeniedException();
    }


    /**
     * Checks if a user is eligible for two-factor authentication based on their login attempts.
     *
     * @param loginDTO The login credentials, including the username.
     * @return True if the user is eligible for two-factor authentication; otherwise, false.
     * @throws DataValidationException if the input data is invalid.
     * @throws ResourceNotFoundException if the user is not found.
     */
    public boolean isReadyFor2FactorAuth(LoginDTO loginDTO) {
        if (!validationUtil.isValid(loginDTO)) {
            throw new DataValidationException();
        }
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(loginDTO.getUsername());

        if (userEntityOptional.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        UserEntity userEntity = userEntityOptional.get();
        return userEntity.getNumberOfLogins() >= MAX_LOGINS;
    }


    /**
     * Sends a two-factor authentication code via email to the user for two-factor authentication.
     *
     * @param loginDTO The login credentials, including the username and email address.
     * @throws MessagingException if there is an issue with sending the two-factor authentication email.
     * @throws EmailSendingException if the email sending process encounters an error.
     */
    public void sendTwoFactorAuthEmail(LoginDTO loginDTO) throws MessagingException {
        String emailResult = emailService.send2FactorAuthEmail(loginDTO);

        if (!emailResult.equals(TWO_FACTOR_CODE_EMAIL_SENT_SUCCESSFULLY)) {
            throw new EmailSendingException();
        }
    }


}
