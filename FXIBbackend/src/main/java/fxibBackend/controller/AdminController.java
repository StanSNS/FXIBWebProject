package fxibBackend.controller;
import fxibBackend.dto.AdminDTOS.AdminUserDTO;
import fxibBackend.dto.AdminDTOS.RolesAdminDTO;
import fxibBackend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import static fxibBackend.constants.MappingConstants.ADMIN_CONTROLLER_MAPPING;
import static fxibBackend.constants.ResponseConst.USER_BANNED_SUCCESSFULLY;
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
     * Retrieves a list of AdminUserDTO objects for users without the 'ADMIN' role.
     *
     * @param jwtToken  JWT token for authentication.
     * @param username  Username of the requester.
     * @return A ResponseEntity containing a list of AdminUserDTO objects, otherwise returns an unauthorized response.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(ADMIN_CONTROLLER_MAPPING)
    public ResponseEntity<List<AdminUserDTO>> getAllUsers(@RequestParam String jwtToken, @RequestParam String username) {
        // Fetch a list of AdminUserDTO objects based on the requester's role and provided parameters
        List<AdminUserDTO> adminUserDTOs = adminService.getAllAdminUserDTO(jwtToken, username);

        // Return a ResponseEntity with the AdminUserDTO list and an HTTP OK status if authorized
        return new ResponseEntity<>(adminUserDTOs, HttpStatus.OK);
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
        // Update user roles and ban status based on provided parameters and requester's role
        adminService.setUserNewRoles(banUsername, roles, jwtToken, loggedUsername);

        // Return a ResponseEntity with a success message and an HTTP OK status if authorized
        return new ResponseEntity<>(USER_BANNED_SUCCESSFULLY, HttpStatus.OK);
    }


}
