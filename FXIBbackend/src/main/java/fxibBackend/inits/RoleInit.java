package fxibBackend.inits;

import fxibBackend.entity.RoleEntity;
import fxibBackend.repository.RoleEntityRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static fxibBackend.constants.RoleConst.*;

@Component
@RequiredArgsConstructor
public class RoleInit {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final RoleEntityRepository roleRepository;

    /**
     * Initializes user roles if no records exist in the RoleRepository.
     * Creates and saves default RoleEntity records for user roles.
     */
    @PostConstruct
    public void rolesInit() {
        // Check if there are no existing RoleEntity records
        if (roleRepository.count() == 0) {
            // Create and save default RoleEntity records for user roles
            roleRepository.save(new RoleEntity(USER_C));
            roleRepository.save(new RoleEntity(ADMIN_C));
            roleRepository.save(new RoleEntity(BANNED_C));
        }
    }


}
