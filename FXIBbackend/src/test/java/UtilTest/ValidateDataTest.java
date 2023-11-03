package UtilTest;

import fxibBackend.entity.RoleEntity;
import fxibBackend.entity.UserEntity;
import fxibBackend.exception.AccessDeniedException;
import fxibBackend.exception.JwtAuthenticationException;
import fxibBackend.repository.UserEntityRepository;
import fxibBackend.util.ValidateData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ValidateDataTest {

    private ValidateData validateData;
    private UserEntityRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserEntityRepository.class);
        validateData = new ValidateData(userRepository);
    }

    @Test
    void testValidateUserWithJWT_ValidUserAndToken() {
        String username = "testUser";
        String jwtToken = "validToken";
        UserEntity userEntity = new UserEntity();
        userEntity.setJwtToken(jwtToken);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        UserEntity result = validateData.validateUserWithJWT(username, jwtToken);

        assertNotNull(result);
    }

    @Test
    void testValidateUserWithJWT_InvalidUser() {
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
    }

    @Test
    void testValidateUserWithJWT_InvalidToken() {
        String username = "testUser";
        String jwtToken = "invalidToken";
        UserEntity userEntity = new UserEntity();
        userEntity.setJwtToken("validToken");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        assertThrows(JwtAuthenticationException.class, () -> validateData.validateUserWithJWT(username, jwtToken));
    }

    @Test
    void testValidateUserAuthority_AdminUser() {
        String username = "adminUser";
        UserEntity userEntity = new UserEntity();
        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity adminRole = new RoleEntity();
        adminRole.setName("ADMIN");
        roles.add(adminRole);
        userEntity.setRoles(roles);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        validateData.validateUserAuthority(username);
    }

    @Test
    void testValidateUserAuthority_NonAdminUser() {
        String username = "regularUser";
        UserEntity userEntity = new UserEntity();
        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity userRole = new RoleEntity();
        userRole.setName("USER");
        roles.add(userRole);
        userEntity.setRoles(roles);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        assertThrows(AccessDeniedException.class, () -> validateData.validateUserAuthority(username));
    }

    @Test
    void testIsUserAdmin() {
        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity adminRole = new RoleEntity();
        adminRole.setName("ADMIN");
        roles.add(adminRole);

        assertTrue(validateData.isUserAdmin(roles));
    }

    @Test
    void testIsUserBanned() {
        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity bannedRole = new RoleEntity();
        bannedRole.setName("BANNED");
        roles.add(bannedRole);

        assertTrue(validateData.isUserBanned(roles));
    }

    @Test
    void testIsUserNotBanned() {
        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity bannedRole = new RoleEntity();
        bannedRole.setName("NOT BANNED");
        roles.add(bannedRole);

        assertFalse(validateData.isUserBanned(roles));
    }

}
