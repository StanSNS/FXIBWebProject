package fxibBackend.controller;

import com.stripe.exception.StripeException;
import fxibBackend.dto.UserDetailsDTO.UpdateUserBiographyDTO;
import fxibBackend.exception.MissingParameterException;
import fxibBackend.service.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static fxibBackend.constants.ActionConst.GET_ALL_USER_DETAILS;
import static fxibBackend.constants.ActionConst.GET_ALL_USER_TRANSACTIONS;
import static fxibBackend.constants.MappingConstants.USER_CUSTOM_LOGOUT_PATHS;
import static fxibBackend.constants.MappingConstants.USER_DETAILS__CONTROLLER_MAPPING;
import static fxibBackend.constants.URLAccessConst.FRONTEND_BASE_URL;

@CrossOrigin(FRONTEND_BASE_URL)
@RequiredArgsConstructor
@RestController
@RequestMapping(USER_DETAILS__CONTROLLER_MAPPING)
public class UserController {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final UserDetailsService userDetailsService;

    /**
     * Retrieves user details or transactions based on the provided action.
     *
     * @param action   The action to perform (e.g., GET_ALL_USER_DETAILS or GET_ALL_USER_TRANSACTIONS).
     * @param username Username of the user.
     * @param jwtToken JWT token for authentication.
     * @return A ResponseEntity with user details or transactions if available, or an error response if the action is unsupported.
     * @throws StripeException if there's an issue with Stripe transactions.
     */
    @PreAuthorize("hasAnyRole('ADMIN','USER','BANNED')")
    @GetMapping
    public ResponseEntity<?> getUserDetails(@RequestParam String action, @RequestParam String username, @RequestParam String jwtToken) throws StripeException {
        // Handle user details or transactions request based on the provided action
        return switch (action) {
            case GET_ALL_USER_DETAILS -> new ResponseEntity<>(userDetailsService.getUserDetailsDTO(username, jwtToken), HttpStatus.OK);
            case GET_ALL_USER_TRANSACTIONS -> new ResponseEntity<>(userDetailsService.getAllUserTransactions(username, jwtToken), HttpStatus.OK);
            default -> throw new MissingParameterException();
        };
    }

    /**
     * Updates the user's biography.
     *
     * @param username  Username of the user.
     * @param jwtToken  JWT token for authentication.
     * @param biography New biography for the user.
     * @return A ResponseEntity with an updated user biography if successful, or an error response if the operation fails.
     */
    @PreAuthorize("hasAnyRole('ADMIN','USER','BANNED')")
    @PutMapping
    public ResponseEntity<UpdateUserBiographyDTO> updateUserBiography(@RequestParam("username") String username, @RequestParam String jwtToken, @RequestParam("biography") String biography) {
        // Update the user's biography and return the updated user biography
        return new ResponseEntity<>(userDetailsService.getUpdateUserBiographyDTO(username, jwtToken, biography), HttpStatus.OK);
    }

    /**
     * Logs the user out.
     *
     * @param username Username of the user.
     * @param jwtToken JWT token for authentication.
     * @return A ResponseEntity with a success message if the logout is successful, or an error response if the operation fails.
     */
    @PreAuthorize("hasAnyRole('ADMIN','USER','BANNED')")
    @PostMapping(USER_CUSTOM_LOGOUT_PATHS)
    public ResponseEntity<?> logoutUser(@RequestParam String username, @RequestParam String jwtToken) {
        // Log the user out and return the appropriate response
        return new ResponseEntity<>(userDetailsService.logoutUser(username, jwtToken), HttpStatus.OK);
    }


}
