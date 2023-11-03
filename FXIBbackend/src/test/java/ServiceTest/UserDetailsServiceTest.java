package ServiceTest;

import com.stripe.exception.StripeException;
import fxibBackend.dto.UserDetailsDTO.StripeTransactionDTO;
import fxibBackend.dto.UserDetailsDTO.UserDetailsDTO;
import fxibBackend.entity.TransactionEntity;
import fxibBackend.entity.UserEntity;
import fxibBackend.exception.AccessDeniedException;
import fxibBackend.exception.DataValidationException;
import fxibBackend.exception.ResourceNotFoundException;
import fxibBackend.repository.TransactionEntityRepository;
import fxibBackend.repository.UserEntityRepository;
import fxibBackend.service.StripeService;
import fxibBackend.service.UserDetailsService;
import fxibBackend.util.ValidateData;
import fxibBackend.util.ValidationUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fxibBackend.constants.OtherConst.*;
import static fxibBackend.constants.ResponseConst.SUCCESSFUL_LOGOUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserDetailsServiceTest {

    private UserDetailsService userDetailsService;
    @Mock
    private UserEntityRepository userRepository;
    @Mock
    private StripeService stripeService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ValidateData validateData;
    @Mock
    private ValidationUtil validationUtil;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private TransactionEntityRepository transactionEntityRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userDetailsService = new UserDetailsService(
                userRepository,
                stripeService,
                passwordEncoder,
                validateData,
                validationUtil,
                modelMapper,
                transactionEntityRepository
        );
    }

    @Test
    public void testGetUserDetailsDTO() throws StripeException {
        String username = "testUser";
        String jwtToken = "testToken";
        UserEntity userEntity = new UserEntity();
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO();

        when(validationUtil.isValid(userEntity)).thenReturn(false);
        when(validationUtil.isValid(userDetailsDTO)).thenReturn(true);

        when(validateData.validateUserWithJWT(username, jwtToken)).thenReturn(userEntity);
        when(modelMapper.map(eq(userEntity), eq(UserDetailsDTO.class))).thenReturn(userDetailsDTO);
        when(stripeService.getAllTransactionsFromEmail(userEntity.getEmail())).thenReturn(new ArrayList<>());

        assertThrows(DataValidationException.class, () -> userDetailsService.getUserDetailsDTO(username, jwtToken));

        verify(validationUtil).isValid(userEntity);
        verify(validationUtil, never()).isValid(userDetailsDTO);
    }

    @Test
    public void testGetUpdateUserBiographyDTOValidationFailure() {
        String username = "testUser";
        String jwtToken = "testToken";
        String biography = "Test biography";
        UserEntity userEntity = new UserEntity();

        when(validateData.validateUserWithJWT(username, jwtToken)).thenReturn(userEntity);
        when(validationUtil.isValid(any(UserEntity.class))).thenReturn(false);

        assertThrows(DataValidationException.class, () -> userDetailsService.getUpdateUserBiographyDTO(username, jwtToken, biography));

        ArgumentCaptor<UserEntity> userEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(validationUtil).isValid(userEntityCaptor.capture());

    }

    @Test
    public void testGetAllUserTransactions() throws StripeException {
        String username = "testUser";
        String jwtToken = "testToken";
        UserEntity userEntity = new UserEntity();
        List<StripeTransactionDTO> transactionsFromEmailDTOs = new ArrayList<>();
        List<TransactionEntity> allByUserEmail = new ArrayList<>();

        when(validateData.validateUserWithJWT(username, jwtToken)).thenReturn(userEntity);
        when(stripeService.getAllTransactionsFromEmail(userEntity.getEmail())).thenReturn(transactionsFromEmailDTOs);
        when(transactionEntityRepository.findAllByUserEmail(userEntity.getEmail())).thenReturn(allByUserEmail);

        verify(transactionEntityRepository, times(0)).saveAll(anyList());
    }

    @Test
    public void testLogoutUser() {
        String username = "testUser";
        String jwtToken = "testToken";
        UserEntity userEntity = new UserEntity();

        when(validateData.validateUserWithJWT(username, jwtToken)).thenReturn(userEntity);

        String result = userDetailsService.logoutUser(username, jwtToken);

        assertEquals(SUCCESSFUL_LOGOUT, result);

        verify(userRepository).save(userEntity);
    }

    @Test
    public void testResetPasswordByTokenInvalidToken() {
        String resetToken = "resetToken";
        String newPassword = "newPassword";
        String hashedToken = DigestUtils.sha256Hex(resetToken);

        when(userRepository.findUserEntityByResetToken(hashedToken)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userDetailsService.resetPasswordByToken(resetToken, newPassword));

        verifyNoInteractions(passwordEncoder, validationUtil);
    }

    @Test
    public void testResetPasswordByTokenMismatchedToken() {
        String resetToken = "resetToken";
        String newPassword = "newPassword";
        String hashedToken = DigestUtils.sha256Hex(resetToken);

        UserEntity userEntity = new UserEntity();
        userEntity.setResetToken("differentToken");

        when(userRepository.findUserEntityByResetToken(hashedToken)).thenReturn(Optional.of(userEntity));

        assertThrows(AccessDeniedException.class, () -> userDetailsService.resetPasswordByToken(resetToken, newPassword));

        verifyNoInteractions(passwordEncoder, validationUtil);
    }

    @Test
    public void testGenerateSubscriptionPlanName() {
        assertEquals(DURATION_LEVEL_ONE, userDetailsService.generateSubscriptionPlanName(PLAN_DURATION_ONE));
        assertEquals(DURATION_LEVEL_TWO, userDetailsService.generateSubscriptionPlanName(PLAN_DURATION_TWO));
        assertEquals(DURATION_LEVEL_THREE, userDetailsService.generateSubscriptionPlanName(PLAN_DURATION_THREE));
        assertEquals(DEFAULT_USER_SUBSCRIPTION, userDetailsService.generateSubscriptionPlanName("otherDuration"));
    }


    @Test
    void testResetUserPassword() {
        String username = "testuser";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        String jwtToken = "yourToken";

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(passwordEncoder.encode(oldPassword));

        when(validateData.validateUserWithJWT(username, jwtToken)).thenReturn(userEntity);
        when(passwordEncoder.matches(oldPassword, userEntity.getPassword())).thenReturn(true);
        when(validationUtil.isValid(userEntity)).thenReturn(true);

        String result = userDetailsService.resetUserPassword(username, oldPassword, newPassword, jwtToken);

        assertEquals("Passwords matches.", result);

        verify(userRepository, times(1)).save(userEntity);
    }

}
