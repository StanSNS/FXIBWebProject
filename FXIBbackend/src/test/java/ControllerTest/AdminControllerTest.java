package ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import fxibBackend.controller.AdminController;
import fxibBackend.dto.AdminDTOS.AdminUserDTO;
import fxibBackend.dto.AdminDTOS.InquiryDTO;
import fxibBackend.dto.AdminDTOS.RolesAdminDTO;
import fxibBackend.exception.MissingParameterException;
import fxibBackend.service.AdminService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AdminControllerTest {

    public final static String ADMIN_CONTROLLER_MAPPING = "/admin";
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
                        .param("action", "getAllUsersAsAdmin")
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

        mockMvc.perform(put(ADMIN_CONTROLLER_MAPPING)
                        .param("jwtToken", jwtToken)
                        .param("loggedUsername", loggedUsername)
                        .param("banUsername", banUsername)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(roles)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(adminService, times(1)).banUser(banUsername, roles, jwtToken, loggedUsername);
    }


    @Test
    public void testGetAllInquiriesForUser() throws Exception {
        InquiryDTO inquiryDTO1 = new InquiryDTO();
        inquiryDTO1.setCustomID("ID_1");
        inquiryDTO1.setTitle("Inquiry 1");
        inquiryDTO1.setDate("2023-01-01");

        InquiryDTO inquiryDTO2 = new InquiryDTO();
        inquiryDTO2.setCustomID("ID_2");
        inquiryDTO2.setTitle("Inquiry 2");
        inquiryDTO2.setDate("2023-01-02");

        List<InquiryDTO> inquiryList = Arrays.asList(inquiryDTO1, inquiryDTO2);

        when(adminService.getAllInquiriesForUser(null, "jwtToken", "username")).thenReturn(inquiryList);

        ResponseEntity<?> response = adminController.getCommands("getAllInquiriesForUser", null, "jwtToken", "username");

        // Verify that the response is as expected
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(inquiryList, response.getBody());
    }

    @Test
    public void testMissingParameterException() {
        // Test the case where a MissingParameterException is thrown.

        // Mock the behavior of your service method to throw MissingParameterException
        when(adminService.getAllInquiriesForUser(any(), any(), any())).thenThrow(MissingParameterException.class);

        // Call the controller method and expect an exception
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(MissingParameterException.class, () ->
                adminController.getCommands("non-existing-path", null, "jwtToken", "username"));

        // Verify that the exception is of type MissingParameterException
        assertEquals(MissingParameterException.class, exception.getClass());
    }

}
