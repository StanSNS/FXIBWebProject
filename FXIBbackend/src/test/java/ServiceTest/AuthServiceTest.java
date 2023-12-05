package ServiceTest;

import fxibBackend.dto.AuthorizationDTOS.JwtAuthResponseDTO;
import fxibBackend.dto.AuthorizationDTOS.LoginDTO;
import fxibBackend.dto.AuthorizationDTOS.RegisterDTO;
import fxibBackend.dto.AuthorizationDTOS.UserRegisterUsernameAndEmailDTO;
import fxibBackend.entity.LocationEntity;
import fxibBackend.entity.RoleEntity;
import fxibBackend.entity.UserEntity;
import fxibBackend.exception.AccessDeniedException;
import fxibBackend.exception.DataValidationException;
import fxibBackend.exception.EmailSendingException;
import fxibBackend.exception.ResourceNotFoundException;
import fxibBackend.repository.RoleEntityRepository;
import fxibBackend.repository.UserEntityRepository;
import fxibBackend.security.JWT.JwtTokenProvider;
import fxibBackend.service.EmailService;
import fxibBackend.service.LocationService;
import fxibBackend.util.CustomDateFormatter;
import fxibBackend.util.ValidationUtil;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static fxibBackend.constants.ConfigConst.MAX_LOGINS;
import static fxibBackend.constants.ResponseConst.TWO_FACTOR_CODE_EMAIL_SENT_SUCCESSFULLY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class AuthServiceTest {

    private static final String USER_REGISTER_SUCCESSFULLY = "User registered successfully!";
    @Mock
    private UserEntityRepository userRepository;
    @Mock
    private RoleEntityRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private ValidationUtil validationUtil;
    @Mock
    private EmailService emailService;
    @Mock
    private LocationService locationService;

    @Mock
    private CustomDateFormatter customDateFormatter;
    private fxibBackend.service.AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new fxibBackend.service.AuthService(
                userRepository, roleRepository, passwordEncoder,
                authenticationManager, jwtTokenProvider, modelMapper,
                validationUtil, emailService, locationService, customDateFormatter
        );
    }

    @Test
    public void testRegister_Success() throws MessagingException {
        RegisterDTO validRegisterDto = new RegisterDTO();
        validRegisterDto.setUsername("testUser");
        validRegisterDto.setEmail("test@example.com");
        validRegisterDto.setPassword("testPassword");

        when(validationUtil.isValid(validRegisterDto)).thenReturn(true);

        when(userRepository.existsByUsername(validRegisterDto.getUsername())).thenReturn(false);

        when(roleRepository.findByName("USER")).thenReturn(new RoleEntity());

        when(userRepository.count()).thenReturn(0L);

        when(locationService.getTheInitialLocation(validRegisterDto.getUsername())).thenReturn(new LocationEntity());

        String result = authService.register(validRegisterDto);

        assertEquals(USER_REGISTER_SUCCESSFULLY, result);

        Mockito.verify(userRepository).save(Mockito.any(UserEntity.class));
    }

    @Test
    public void testGetAllUsernamesAndEmails_Success() {
        List<UserEntity> userEntities = new ArrayList<>();
        userEntities.add(createUserEntity("user1", "user1@example.com"));
        userEntities.add(createUserEntity("user2", "user2@example.com"));
        when(userRepository.findAll()).thenReturn(userEntities);

        when(validationUtil.isValid(Mockito.any(UserRegisterUsernameAndEmailDTO.class))).thenAnswer(invocation -> {
            UserRegisterUsernameAndEmailDTO dto = invocation.getArgument(0);
            return dto.getUsername() != null && dto.getEmail() != null;
        });

        when(modelMapper.map(Mockito.any(UserEntity.class), Mockito.eq(UserRegisterUsernameAndEmailDTO.class))
        ).thenAnswer(invocation -> {
            UserEntity userEntity = invocation.getArgument(0);
            UserRegisterUsernameAndEmailDTO dto = new UserRegisterUsernameAndEmailDTO();
            dto.setUsername(userEntity.getUsername());
            dto.setEmail(userEntity.getEmail());
            return dto;
        });

        List<UserRegisterUsernameAndEmailDTO> result = authService.getAllUsernamesAndEmails();

        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("user1@example.com", result.get(0).getEmail());
        assertEquals("user2", result.get(1).getUsername());
        assertEquals("user2@example.com", result.get(1).getEmail());
    }

    @Test
    public void testGetAllUsernamesAndEmails_InvalidData() {
        List<UserEntity> userEntities = new ArrayList<>();
        userEntities.add(createUserEntity("user1", "user1@example.com"));
        when(userRepository.findAll()).thenReturn(userEntities);
        assertThrows(DataValidationException.class, () -> authService.getAllUsernamesAndEmails());
    }

    private UserEntity createUserEntity(String username, String email) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setEmail(email);
        return userEntity;
    }

    @Test
    public void testIsReadyFor2FactorAuth_Ready() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testUser");

        when(validationUtil.isValid(loginDTO)).thenReturn(true);

        UserEntity userEntity = new UserEntity();
        userEntity.setNumberOfLogins(MAX_LOGINS);
        when(userRepository.findByUsername(loginDTO.getUsername())).thenReturn(Optional.of(userEntity));

        boolean result = authService.isReadyFor2FactorAuth(loginDTO);

        assertTrue(result);
    }

    @Test
    public void testIsReadyFor2FactorAuth_NotReady() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testUser");

        when(validationUtil.isValid(loginDTO)).thenReturn(true);

        UserEntity userEntity = new UserEntity();
        userEntity.setNumberOfLogins(MAX_LOGINS - 1); // Less than MAX_LOGINS
        when(userRepository.findByUsername(loginDTO.getUsername())).thenReturn(Optional.of(userEntity));

        boolean result = authService.isReadyFor2FactorAuth(loginDTO);

        assertFalse(result);
    }

    @Test
    public void testIsReadyFor2FactorAuth_InvalidData() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testUser");

        when(validationUtil.isValid(loginDTO)).thenReturn(false);

        assertThrows(DataValidationException.class, () -> authService.isReadyFor2FactorAuth(loginDTO));
    }

    @Test
    public void testIsReadyFor2FactorAuth_UserNotFound() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("nonExistentUser");

        when(validationUtil.isValid(loginDTO)).thenReturn(true);

        when(userRepository.findByUsername(loginDTO.getUsername())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.isReadyFor2FactorAuth(loginDTO));
    }

    @Test
    public void testSendTwoFactorAuthEmail_Success() throws MessagingException {
        LoginDTO loginDTO = new LoginDTO();

        when(emailService.send2FactorAuthEmail(loginDTO)).thenReturn(TWO_FACTOR_CODE_EMAIL_SENT_SUCCESSFULLY);

        assertDoesNotThrow(() -> authService.sendTwoFactorAuthEmail(loginDTO));
    }

    @Test
    public void testSendTwoFactorAuthEmail_Failure() throws MessagingException {
        LoginDTO loginDTO = new LoginDTO();

        when(emailService.send2FactorAuthEmail(loginDTO)).thenReturn("Email sending failed");

        assertThrows(EmailSendingException.class, () -> authService.sendTwoFactorAuthEmail(loginDTO));
    }

    @Test
    public void testTwoFactorAuthLogin_InvalidCode() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testUser");
        String code = "123456";

        UserEntity userEntity = new UserEntity();
        userEntity.setTwoFactorLoginCode(654321);
        when(userRepository.findByUsername(loginDTO.getUsername())).thenReturn(Optional.of(userEntity));

        assertThrows(AccessDeniedException.class, () -> authService.twoFactorAuthLogin(loginDTO, code));
    }

    @Test
    public void testLogin_Success() throws MessagingException {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testUser");
        loginDTO.setPassword("testPassword");

        when(validationUtil.isValid(loginDTO)).thenReturn(true);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setJwtToken(null);
        userEntity.setRoles(Collections.singleton(new RoleEntity("ROLE_USER")));
        LocationEntity locationEntity = new LocationEntity();
        locationEntity.setUsername("testLocation");
        userEntity.setLocationEntity(locationEntity);
        userEntity.setNumberOfLogins(1);

        when(userRepository.findByUsername(loginDTO.getUsername())).thenReturn(Optional.of(userEntity));

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        when(jwtTokenProvider.validateToken(null)).thenReturn(false);

        when(jwtTokenProvider.generateToken(authentication)).thenReturn("testToken");

        when(validationUtil.isValid(Mockito.any(JwtAuthResponseDTO.class))).thenReturn(true);

        JwtAuthResponseDTO result = authService.login(loginDTO);

        assertEquals("testToken", result.getAccessToken());
        assertEquals(Collections.singleton("ROLE_USER"), result.getRole());

        assertTrue(validationUtil.isValid(result));
    }

}
