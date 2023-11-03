package fxibBackend.security;

import fxibBackend.entity.RoleEntity;
import fxibBackend.entity.UserEntity;
import fxibBackend.repository.UserEntityRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static fxibBackend.constants.OtherConst.DEFAULT_USER_PASSWORD;
import static fxibBackend.constants.OtherConst.DEFAULT_USER_USERNAME;
import static fxibBackend.constants.RoleConst.ROLE_PREFIX;
import static fxibBackend.constants.RoleConst.USER_C;

/**
 * Service responsible for loading user details and creating default user details when necessary.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final UserEntityRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Load user details by username from the repository
        UserEntity user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            // If the user is not found, create a default user with minimal authorities
            return createDefaultUser();
        }

        // Extract and build user authorities from roles
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (user.getRoles() != null) {
            for (RoleEntity role : user.getRoles()) {
                authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + role.getName()));
            }
        }

        // Create and return a UserDetails object
        return new User(username, user.getPassword(), authorities);
    }

    /**
     * Creates a default UserDetails object with minimal authorities.
     *
     * @return Default UserDetails for unidentified users.
     */
    public UserDetails createDefaultUser() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + USER_C));
        return new User(DEFAULT_USER_USERNAME, DEFAULT_USER_PASSWORD, authorities);
    }

}



