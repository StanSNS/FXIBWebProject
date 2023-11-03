package ServiceTest;

import fxibBackend.dto.AdminDTOS.AdminUserDTO;
import fxibBackend.dto.AdminDTOS.RolesAdminDTO;
import fxibBackend.entity.RoleEntity;
import fxibBackend.entity.UserEntity;
import fxibBackend.exception.DataValidationException;
import fxibBackend.repository.UserEntityRepository;
import fxibBackend.service.AdminService;
import fxibBackend.util.ValidateData;
import fxibBackend.util.ValidationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private UserEntityRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ValidateData validateData;

    @Mock
    private ValidationUtil validationUtil;

    @Test
    void testGetAllAdminUserDTO() {
        String jwtToken = "mockToken";
        String username = "mockUser";

        UserEntity user1 = new UserEntity();
        user1.setUsername("user1");
        user1.setRoles(new HashSet<>());
        user1.getRoles().add(new RoleEntity("ROLE_USER"));

        UserEntity user2 = new UserEntity();
        user2.setUsername("user2");
        user2.setRoles(new HashSet<>());
        user2.getRoles().add(new RoleEntity("ROLE_ADMIN"));

        List<UserEntity> userEntities = Arrays.asList(user1, user2);

        Mockito.when(userRepository.findAll()).thenReturn(userEntities);

        AdminUserDTO adminUserDTO1 = new AdminUserDTO();
        adminUserDTO1.setUsername("user1");

        AdminUserDTO adminUserDTO2 = new AdminUserDTO();
        adminUserDTO2.setUsername("user2");

        Mockito.when(modelMapper.map(user1, AdminUserDTO.class)).thenReturn(adminUserDTO1);
        Mockito.when(modelMapper.map(user2, AdminUserDTO.class)).thenReturn(adminUserDTO2);

        Mockito.when(validationUtil.isValid(adminUserDTO1)).thenReturn(true);
        Mockito.when(validationUtil.isValid(adminUserDTO2)).thenReturn(true);

        List<AdminUserDTO> adminUserDTOs = adminService.getAllAdminUserDTO(jwtToken, username);

        assertEquals(2, adminUserDTOs.size());
        assertEquals("user1", adminUserDTOs.get(0).getUsername());
    }

    @Test
    void testGetAllAdminUserDTOWithInvalidData() {
        String jwtToken = "mockToken";
        String username = "mockUser";

        UserEntity user1 = new UserEntity();
        user1.setUsername("user1");
        user1.setRoles(new HashSet<>());
        user1.getRoles().add(new RoleEntity("ROLE_USER"));

        List<UserEntity> userEntities = List.of(user1);

        Mockito.when(userRepository.findAll()).thenReturn(userEntities);

        AdminUserDTO adminUserDTO1 = new AdminUserDTO();
        adminUserDTO1.setUsername("user1");

        Mockito.when(modelMapper.map(user1, AdminUserDTO.class)).thenReturn(adminUserDTO1);

        Mockito.when(validationUtil.isValid(adminUserDTO1)).thenReturn(false);

        assertThrows(DataValidationException.class, () -> adminService.getAllAdminUserDTO(jwtToken, username));
    }

    @Test
    public void testSetUserNewRoles() {
        String username = "exampleUsername";
        String jwtToken = "exampleToken";
        String loggedUsername = "exampleLoggedUsername";

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername(username);
        userEntity.setBiography("");

        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(userEntity));

        when(validateData.validateUserWithJWT(loggedUsername, jwtToken)).thenReturn(userEntity);
        when(validateData.isUserBanned(userEntity.getRoles())).thenReturn(false);
        when(validationUtil.isValid(any(RoleEntity.class))).thenReturn(true);

        RolesAdminDTO roleDto = new RolesAdminDTO();
        roleDto.setName("ROLE_ADMIN");
        RoleEntity roleEntity = new RoleEntity("ROLE_ADMIN");
        when(modelMapper.map(roleDto, RoleEntity.class)).thenReturn(roleEntity);

        Set<RolesAdminDTO> roles = Collections.singleton(roleDto);

        adminService.setUserNewRoles(username, roles, jwtToken, loggedUsername);

        verify(userRepository, times(1)).findByUsername(username);
        verify(validateData, times(1)).validateUserWithJWT(loggedUsername, jwtToken);
        verify(validateData, times(1)).validateUserAuthority(loggedUsername);
        verify(validationUtil, times(1)).isValid(roleEntity);
        verify(userRepository, times(1)).save(userEntity);

        assertTrue(userEntity.getRoles().contains(roleEntity));
        assertEquals("Banned User!", userEntity.getBiography());

        when(validateData.isUserBanned(userEntity.getRoles())).thenReturn(true);
        when(validationUtil.isValid(any(RoleEntity.class))).thenReturn(false);

        assertThrows(DataValidationException.class, () -> adminService.setUserNewRoles(username, roles, jwtToken, loggedUsername));
    }

}
