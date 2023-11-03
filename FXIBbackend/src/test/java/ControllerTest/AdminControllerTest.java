package ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import fxibBackend.controller.AdminController;
import fxibBackend.dto.AdminDTOS.AdminUserDTO;
import fxibBackend.dto.AdminDTOS.RolesAdminDTO;
import fxibBackend.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AdminControllerTest {

    public final static String ADMIN_CONTROLLER_MAPPING = "/admin";
    public static final String USER_BANNED_SUCCESSFULLY = "User banned successfully!";
    private MockMvc mockMvc;
    @Mock
    private AdminService adminService;
    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("admin", "password"));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        AdminUserDTO userDTO1 = new AdminUserDTO();
        userDTO1.setUsername("user1");

        AdminUserDTO userDTO2 = new AdminUserDTO();
        userDTO2.setUsername("user2");

        List<AdminUserDTO> userList = Arrays.asList(userDTO1, userDTO2);

        when(adminService.getAllAdminUserDTO("jwtToken", "username")).thenReturn(userList);

        mockMvc.perform(get("/admin")
                        .param("jwtToken", "jwtToken")
                        .param("username", "username"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateBanRoleUser() throws Exception {
        String jwtToken = "jwt-token";
        String loggedUsername = "loggedUser";
        String banUsername = "bannedUser";
        Set<RolesAdminDTO> roles = new HashSet<>();

        doNothing().when(adminService).setUserNewRoles(banUsername, roles, jwtToken, loggedUsername);

        mockMvc.perform(put(ADMIN_CONTROLLER_MAPPING)
                        .param("jwtToken", jwtToken)
                        .param("loggedUsername", loggedUsername)
                        .param("banUsername", banUsername)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(roles)))
                .andExpect(status().isOk())
                .andExpect(content().string(USER_BANNED_SUCCESSFULLY));

        verify(adminService, times(1)).setUserNewRoles(banUsername, roles, jwtToken, loggedUsername);
    }

}
