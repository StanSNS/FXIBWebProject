package SecurityTest;

import fxibBackend.entity.RoleEntity;
import fxibBackend.entity.UserEntity;
import fxibBackend.repository.UserEntityRepository;
import fxibBackend.security.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CustomUserDetailsServiceTest {

    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private UserEntityRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customUserDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test
    void loadUserByUsername_UserFound_ReturnsUserDetails() {
        String username = "testUser";
        RoleEntity role = new RoleEntity();
        role.setName("ROLE_USER");
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword("password");
        userEntity.setRoles(new HashSet<>());
        userEntity.getRoles().add(role);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ROLE_USER"));
        User expectedUserDetails = new User(username, "password", authorities);
        assertEquals(expectedUserDetails, userDetails);
    }

    @Test
    void loadUserByUsername_UserNotFound_ReturnsDefaultUserDetails() {
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        User expectedUserDetails = new User("defaultUser", "defaultPassword", authorities);
        assertEquals(expectedUserDetails, userDetails);
    }

    @Test
    void createDefaultUser_ReturnsDefaultUserDetails() {
        UserDetails userDetails = customUserDetailsService.createDefaultUser();

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        User expectedUserDetails = new User("defaultUser", "defaultPassword", authorities);
        assertEquals(expectedUserDetails, userDetails);
    }

}
