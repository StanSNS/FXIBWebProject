package ServiceTest;

import fxibBackend.dto.AdminDTOS.AdminUserDTO;
import fxibBackend.dto.AdminDTOS.InquiryDTO;
import fxibBackend.dto.AdminDTOS.ReportDTO;
import fxibBackend.dto.AdminDTOS.RolesAdminDTO;
import fxibBackend.entity.InquiryEntity;
import fxibBackend.entity.ReportEntity;
import fxibBackend.entity.RoleEntity;
import fxibBackend.entity.UserEntity;
import fxibBackend.exception.DataValidationException;
import fxibBackend.exception.ResourceNotFoundException;
import fxibBackend.repository.InquiryEntityRepository;
import fxibBackend.repository.ReportEntityRepository;
import fxibBackend.repository.UserEntityRepository;
import fxibBackend.service.AdminService;
import fxibBackend.service.EmailService;
import fxibBackend.util.ValidateData;
import fxibBackend.util.ValidationUtil;
import jakarta.mail.MessagingException;
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

    @Mock
    private EmailService emailService;

    @Mock
    private InquiryEntityRepository inquiryEntityRepository;

    @Mock
    private ReportEntityRepository reportEntityRepository;

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
    public void testSetUserNewRoles() throws MessagingException {
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

        adminService.banUser(username, roles, jwtToken, loggedUsername);

        verify(userRepository, times(1)).findByUsername(username);
        verify(validateData, times(1)).validateUserWithJWT(loggedUsername, jwtToken);
        verify(validateData, times(1)).validateUserAuthority(loggedUsername);
        verify(validationUtil, times(1)).isValid(roleEntity);
        verify(userRepository, times(1)).save(userEntity);

        assertTrue(userEntity.getRoles().contains(roleEntity));
        assertEquals("Banned User!", userEntity.getBiography());

        when(validateData.isUserBanned(userEntity.getRoles())).thenReturn(true);
        when(validationUtil.isValid(any(RoleEntity.class))).thenReturn(false);

        assertThrows(DataValidationException.class, () -> adminService.banUser(username, roles, jwtToken, loggedUsername));
    }


    @Test
    void testGetAllInquiriesForUser() {
        String currentUsername = "mockUser";
        String jwtToken = "mockToken";
        String username = "user1";

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);

        when(validateData.validateUserWithJWT(username, jwtToken)).thenReturn(userEntity);

        InquiryEntity inquiry1 = new InquiryEntity();
        inquiry1.setCustomID("ID_1");
        inquiry1.setTitle("Inquiry 1");
        inquiry1.setContent("Content 1");
        inquiry1.setDate("2023-01-01");
        inquiry1.setUserEntity(userEntity);

        InquiryEntity inquiry2 = new InquiryEntity();
        inquiry2.setCustomID("ID_2");
        inquiry2.setTitle("Inquiry 2");
        inquiry2.setContent("Content 2");
        inquiry2.setDate("2023-01-02");
        inquiry2.setUserEntity(userEntity);

        when(userRepository.findByUsername(currentUsername)).thenReturn(Optional.of(userEntity));
        when(inquiryEntityRepository.findAllByUserEntity_Id(userEntity.getId())).thenReturn(List.of(inquiry1, inquiry2));

        InquiryDTO inquiryDTO1 = new InquiryDTO();
        inquiryDTO1.setCustomID("ID_1");
        inquiryDTO1.setTitle("Inquiry 1");
        inquiryDTO1.setDate("2023-01-01");

        InquiryDTO inquiryDTO2 = new InquiryDTO();
        inquiryDTO2.setCustomID("ID_2");
        inquiryDTO2.setTitle("Inquiry 2");
        inquiryDTO2.setDate("2023-01-02");

        when(modelMapper.map(inquiry1, InquiryDTO.class)).thenReturn(inquiryDTO1);
        when(modelMapper.map(inquiry2, InquiryDTO.class)).thenReturn(inquiryDTO2);

        List<InquiryDTO> result = adminService.getAllInquiriesForUser(currentUsername, jwtToken, username);

        assertEquals(2, result.size());
        assertEquals("ID_1", result.get(0).getCustomID());
        assertEquals("Inquiry 1", result.get(0).getTitle());
        assertEquals("2023-01-01", result.get(0).getDate());
        assertEquals("ID_2", result.get(1).getCustomID());
        assertEquals("Inquiry 2", result.get(1).getTitle());
        assertEquals("2023-01-02", result.get(1).getDate());

        Mockito.verify(validateData, Mockito.times(1)).validateUserWithJWT(username, jwtToken);
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(currentUsername);
        Mockito.verify(inquiryEntityRepository, Mockito.times(1)).findAllByUserEntity_Id(userEntity.getId());
        Mockito.verify(modelMapper, Mockito.times(2)).map(Mockito.any(InquiryEntity.class), Mockito.eq(InquiryDTO.class));
    }


    @Test
    void testGetAllReportsForUser() {
        String currentUsername = "mockUser";
        String jwtToken = "mockToken";
        String username = "user1";

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);

        when(userRepository.findByUsername(currentUsername)).thenReturn(Optional.of(userEntity));
        when(reportEntityRepository.findAllByUserEntity_Id(userEntity.getId())).thenReturn(Collections.emptyList());

        List<ReportDTO> result = adminService.getAllReportsForUser(currentUsername, jwtToken, username);

        assertNotNull(result);
        assertEquals(0, result.size());

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(currentUsername);
        Mockito.verify(reportEntityRepository, Mockito.times(1)).findAllByUserEntity_Id(userEntity.getId());
        Mockito.verify(modelMapper, Mockito.times(0)).map(Mockito.any(ReportEntity.class), Mockito.eq(ReportDTO.class));
    }

    @Test
    void testGetAllReportsForUserWithInvalidUser() {
        String currentUsername = "mockUser";
        String jwtToken = "mockToken";
        String username = "user1";

        when(userRepository.findByUsername(currentUsername)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                adminService.getAllReportsForUser(currentUsername, jwtToken, username));

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(currentUsername);
        Mockito.verify(reportEntityRepository, Mockito.times(0)).findAllByUserEntity_Id(Mockito.anyLong());
        Mockito.verify(modelMapper, Mockito.times(0)).map(Mockito.any(ReportEntity.class), Mockito.eq(ReportDTO.class));
    }


    @Test
    void testGetAllInquiriesForUserWithInvalidUser() {
        String currentUsername = "mockUser";
        String jwtToken = "mockToken";
        String username = "user1";

        when(validateData.validateUserWithJWT(username, jwtToken)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () ->
                adminService.getAllInquiriesForUser(currentUsername, jwtToken, username));

        Mockito.verify(validateData, Mockito.times(1)).validateUserWithJWT(username, jwtToken);
    }

}
