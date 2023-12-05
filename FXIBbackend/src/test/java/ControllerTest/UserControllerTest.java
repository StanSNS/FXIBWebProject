package ControllerTest;

import com.stripe.exception.StripeException;
import fxibBackend.controller.UserController;
import fxibBackend.dto.UserDetailsDTO.StripeTransactionDTO;
import fxibBackend.dto.UserDetailsDTO.UpdateUserBiographyDTO;
import fxibBackend.dto.UserDetailsDTO.UserDetailsDTO;
import fxibBackend.exception.MissingParameterException;
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

import static fxibBackend.constants.ActionConst.*;
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
    void testInquiryReport_ReportProblemAndEmailSend() throws MessagingException {
        ResponseEntity<?> response = userController.inquiryReport(REPORT_PROBLEM_AND_EMAIL_SEND, "title", "content", "imgURL", "username", "jwtToken");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userDetailsService, times(1)).saveReportAndSendEmail("title", "content", "imgURL", "username", "jwtToken");
    }

    @Test
    void testInquiryReport_SendInquiryAndEmailSend() throws MessagingException {
        ResponseEntity<?> response = userController.inquiryReport(SEND_INQUIRY_AND_EMAIL_SEND, "title", "content", null, "username", "jwtToken");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userDetailsService, times(1)).sendInquiryEmailAndSave("title", "content", "username", "jwtToken");
    }

    @Test
    void testInquiryReport_InvalidAction() {
        assertThrows(MissingParameterException.class,
                () -> userController.inquiryReport("INVALID_ACTION", "title", "content", null, "username", "jwtToken"));
    }
}
