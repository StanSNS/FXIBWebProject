package ControllerTest;

import com.stripe.exception.StripeException;
import fxibBackend.controller.UserController;
import fxibBackend.dto.UserDetailsDTO.StripeTransactionDTO;
import fxibBackend.dto.UserDetailsDTO.UpdateUserBiographyDTO;
import fxibBackend.dto.UserDetailsDTO.UserDetailsDTO;
import fxibBackend.exception.AccessDeniedException;
import fxibBackend.exception.MissingParameterException;
import fxibBackend.exception.ResourceNotFoundException;
import fxibBackend.service.UserDetailsService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static fxibBackend.constants.ActionConst.GET_ALL_USER_DETAILS;
import static fxibBackend.constants.ActionConst.GET_ALL_USER_TRANSACTIONS;
import static org.aspectj.bridge.MessageUtil.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserDetailsService userDetailsService;
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userController = new UserController(userDetailsService);
    }

    @Test
    void testGetUserDetailsAllUserDetails() throws StripeException {
        when(userDetailsService.getUserDetailsDTO("testUser", "jwtToken")).thenReturn(new UserDetailsDTO());

        ResponseEntity<?> response = userController.getUserDetails(GET_ALL_USER_DETAILS, "testUser", "jwtToken");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetUserDetailsAllUserTransactions() throws StripeException {
        List<StripeTransactionDTO> stripeTransactionDTOS = new ArrayList<>();
        when(userDetailsService.getAllUserTransactions("testUser", "jwtToken")).thenReturn(stripeTransactionDTOS);

        ResponseEntity<?> response = userController.getUserDetails(GET_ALL_USER_TRANSACTIONS, "testUser", "jwtToken");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateUserBiography() {
        UpdateUserBiographyDTO updateBiographyDTO = new UpdateUserBiographyDTO();
        when(userDetailsService.getUpdateUserBiographyDTO("testUser", "jwtToken", "New Biography")).thenReturn(updateBiographyDTO);

        ResponseEntity<UpdateUserBiographyDTO> response = userController.updateUserBiography("testUser", "jwtToken", "New Biography");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updateBiographyDTO, response.getBody());
    }

    @Test
    void testLogoutUser() {
        when(userDetailsService.logoutUser("testUser", "jwtToken")).thenReturn("UserLogoutDetails");

        ResponseEntity<?> response = userController.logoutUser("testUser", "jwtToken");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("UserLogoutDetails", response.getBody());
    }

    @Test
    void testInvalidAction() throws StripeException {
        when(userDetailsService.getUserDetailsDTO("testUser", "jwtToken")).thenThrow(new MissingParameterException());

        try {
            ResponseEntity<?> response = userController.getUserDetails("InvalidAction", "testUser", "jwtToken");
            fail("Expected MissingParameterException but no exception was thrown.");
        } catch (MissingParameterException e) {

        }
    }

    @Test
    void testSendInquiryEmail() throws MessagingException {
        ResponseEntity<?> response = userController.sendInquiryEmail("Test Title", "Test Content", "testUser", "jwtToken");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userDetailsService).sendInquiryEmailAndSave("Test Title", "Test Content", "testUser", "jwtToken");
    }

    @Test
    void testSendInquiryEmailWithMissingParameters() throws MessagingException {
        doThrow(new MissingParameterException()).when(userDetailsService)
                .sendInquiryEmailAndSave("Test Title", "Test Content", "testUser", "jwtToken");

        assertThrows(MissingParameterException.class,() -> userController.sendInquiryEmail("Test Title", "Test Content", "testUser", "jwtToken"));
    }


}
