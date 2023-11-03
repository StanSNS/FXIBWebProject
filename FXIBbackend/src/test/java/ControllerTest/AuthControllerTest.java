package ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import fxibBackend.controller.AuthController;
import fxibBackend.dto.AuthorizationDTOS.JwtAuthResponseDTO;
import fxibBackend.dto.AuthorizationDTOS.LoginDTO;
import fxibBackend.dto.AuthorizationDTOS.RegisterDTO;
import fxibBackend.dto.AuthorizationDTOS.UserRegisterUsernameAndEmailDTO;
import fxibBackend.service.AuthService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static fxibBackend.constants.ResponseConst.USER_REGISTER_SUCCESSFULLY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AuthControllerTest {

    public final static String AUTH_CONTROLLER_MAPPING_REGISTER = "/auth/register";
    public final static String AUTH_CONTROLLER_MAPPING_GET_ALL_USERNAMES = "/auth/register";
    public final static String AUTH_CONTROLLER_MAPPING_LOGIN = "/auth/login";
    public final static String TWO_FACTOR_AUTH_CONTROLLER_MAPPING_LOGIN = "/auth/login/two-factor-auth";
    private MockMvc mockMvc;
    @Mock
    private AuthService authService;
    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("admin", "password"));
    }

    @Test
    public void testRegister() throws Exception {
        RegisterDTO registerDto = new RegisterDTO();

        when(authService.register(registerDto)).thenReturn(USER_REGISTER_SUCCESSFULLY);

        mockMvc.perform(post(AUTH_CONTROLLER_MAPPING_REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetAllUserUsernameAndEmails() throws Exception {
        List<UserRegisterUsernameAndEmailDTO> users = new ArrayList<>();

        when(authService.getAllUsernamesAndEmails()).thenReturn(users);

        mockMvc.perform(get(AUTH_CONTROLLER_MAPPING_GET_ALL_USERNAMES))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(users)));
    }

    @Test
    public void testLogin() throws Exception {
        LoginDTO loginDto = new LoginDTO();

        JwtAuthResponseDTO jwtAuthResponseDTO = new JwtAuthResponseDTO();
        when(authService.isReadyFor2FactorAuth(loginDto)).thenReturn(false);
        when(authService.login(loginDto)).thenReturn(jwtAuthResponseDTO);

        mockMvc.perform(post(AUTH_CONTROLLER_MAPPING_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testLogin2FA() throws Exception {
        String username = "testUser";
        String password = "testPassword";
        String code = "123456";

        LoginDTO loginDto = new LoginDTO();

        JwtAuthResponseDTO jwtAuthResponseDTO = new JwtAuthResponseDTO();

        when(authService.twoFactorAuthLogin(loginDto, code)).thenReturn(jwtAuthResponseDTO);

        mockMvc.perform(post(TWO_FACTOR_AUTH_CONTROLLER_MAPPING_LOGIN)
                        .param("username", username)
                        .param("password", password)
                        .param("code", code))
                .andExpect(status().isOk());
    }

    @Test
    void test2FALoginRedirect() throws MessagingException {
        LoginDTO loginDto = new LoginDTO("testUser", "password");
        when(authService.isReadyFor2FactorAuth(loginDto)).thenReturn(true);

        ResponseEntity<?> response = authController.login(loginDto);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Redirect to 2FA Login", response.getBody());
    }

}
