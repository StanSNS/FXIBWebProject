package fxibBackend.util;

import fxibBackend.entity.RoleEntity;
import fxibBackend.entity.UserEntity;
import fxibBackend.exception.AccessDeniedException;
import fxibBackend.exception.JwtAuthenticationException;
import fxibBackend.exception.ResourceNotFoundException;
import fxibBackend.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

import static fxibBackend.constants.RoleConst.ADMIN_C;
import static fxibBackend.constants.RoleConst.BANNED_C;

@Service
@RequiredArgsConstructor
public class ValidateData {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final UserEntityRepository userRepository;

    /**
     * Validates a user with a JWT token by checking if the provided username and token match a user in the database.
     *
     * @param username  The username of the user to validate.
     * @param jwtToken  The JWT token to verify.
     * @return The validated UserEntity if successful.
     * @throws ResourceNotFoundException      If the user with the provided username is not found.
     * @throws JwtAuthenticationException     If the provided JWT token is invalid.
     */
    public UserEntity validateUserWithJWT(String username, String jwtToken) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);
        if (userEntityOptional.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        UserEntity userEntity = userEntityOptional.get();
        if (!userEntity.getJwtToken().equals(jwtToken)) {
            throw new JwtAuthenticationException();
        }
        return userEntity;
    }

    /**
     * Validates user authority by checking if the user has admin privileges.
     *
     * @param username  The username of the user to validate.
     * @throws AccessDeniedException If the user does not have admin privileges.
     */
    public void validateUserAuthority(String username) {
        UserEntity userEntity = userRepository.findByUsername(username).get();
        if (!isUserAdmin(userEntity.getRoles())) {
            throw new AccessDeniedException();
        }

    }

    /**
     * Checks if a user has admin privileges based on their roles.
     *
     * @param roles  The set of roles associated with the user.
     * @return True if the user has admin privileges, otherwise false.
     */
    public boolean isUserAdmin(Set<RoleEntity> roles) {
        for (RoleEntity role : roles) {
            if (role.getName().equals(ADMIN_C)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a user is banned based on their roles.
     *
     * @param roles  The set of roles associated with the user.
     * @return True if the user is banned, otherwise false.
     */
    public boolean isUserBanned(Set<RoleEntity> roles) {
        for (RoleEntity role : roles) {
            if (role.getName().equals(BANNED_C)) {
                return true;
            }
        }
        return false;
    }



}
