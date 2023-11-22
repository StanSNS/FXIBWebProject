package fxibBackend.controller;

import fxibBackend.dto.AdminDTOS.RolesAdminDTO;
import fxibBackend.exception.MissingParameterException;
import fxibBackend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static fxibBackend.constants.ActionConst.GET_ALL_INQUIRIES_FOR_USER;
import static fxibBackend.constants.ActionConst.GET_ALL_USERS_AS_ADMIN;
import static fxibBackend.constants.MappingConstants.ADMIN_CONTROLLER_MAPPING;
import static fxibBackend.constants.URLAccessConst.FRONTEND_BASE_URL;

@CrossOrigin(FRONTEND_BASE_URL)
@RequiredArgsConstructor
@RestController
@RequestMapping()
public class AdminController {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final AdminService adminService;



    /**
     * Retrieves various administrative commands based on the provided action parameter.
     *
     * @param action           The action to be performed, such as retrieving all users or inquiries for a user.
     * @param currentUsername  Username of the requester making the request.
     * @param jwtToken         JWT token for authentication.
     * @param username         Username relevant to the action (e.g., target user for inquiries).
     * @return A ResponseEntity containing the result of the specified administrative action.
     * @throws MissingParameterException if the action parameter is not recognized.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(ADMIN_CONTROLLER_MAPPING)
    public ResponseEntity<?> getCommands(@RequestParam String action,
                                         @RequestParam(required = false) String currentUsername,
                                         @RequestParam String jwtToken,
                                         @RequestParam String username) {

        return switch (action) {
            case GET_ALL_USERS_AS_ADMIN -> new ResponseEntity<>(adminService.getAllAdminUserDTO(jwtToken, username), HttpStatus.OK);
            case GET_ALL_INQUIRIES_FOR_USER -> new ResponseEntity<>(adminService.getAllInquiriesForUser(currentUsername, jwtToken, username), HttpStatus.OK);
            default -> throw new MissingParameterException();
        };
    }

    /**
     * Updates the roles of a user and marks them as banned or unbanned.
     *
     * @param jwtToken       JWT token for authentication.
     * @param loggedUsername Username of the requester making the role update.
     * @param banUsername    Username of the user to update roles for.
     * @param roles          The new roles to assign to the user.
     * @return A ResponseEntity with a success message if the update is successful, or an error response if unauthorized.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(ADMIN_CONTROLLER_MAPPING)
    public ResponseEntity<String> updateBanRoleUser(@RequestParam String jwtToken, @RequestParam String loggedUsername, @RequestParam String banUsername, @RequestBody Set<RolesAdminDTO> roles) {
        // Return a ResponseEntity with a success message and an HTTP OK status if authorized
        return new ResponseEntity<>(adminService.setUserNewRoles(banUsername, roles, jwtToken, loggedUsername), HttpStatus.OK);
    }


}
