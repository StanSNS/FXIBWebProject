package fxibBackend.service;


import com.stripe.exception.StripeException;
import fxibBackend.dto.UserDetailsDTO.StripeTransactionDTO;
import fxibBackend.dto.UserDetailsDTO.UpdateUserBiographyDTO;
import fxibBackend.dto.UserDetailsDTO.UserDetailsDTO;
import fxibBackend.entity.InquiryEntity;
import fxibBackend.entity.ReportEntity;
import fxibBackend.entity.TransactionEntity;
import fxibBackend.entity.UserEntity;
import fxibBackend.exception.AccessDeniedException;
import fxibBackend.exception.DataValidationException;
import fxibBackend.exception.ResourceNotFoundException;
import fxibBackend.repository.InquiryEntityRepository;
import fxibBackend.repository.ReportEntityRepository;
import fxibBackend.repository.TransactionEntityRepository;
import fxibBackend.repository.UserEntityRepository;
import fxibBackend.util.CustomDateFormatter;
import fxibBackend.util.ValidateData;
import fxibBackend.util.ValidationUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static fxibBackend.constants.OtherConst.*;
import static fxibBackend.constants.ResponseConst.*;

@Service
@RequiredArgsConstructor
public class UserDetailsService {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final UserEntityRepository userRepository;
    private final StripeService stripeService;
    private final PasswordEncoder passwordEncoder;
    private final ValidateData validateData;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final TransactionEntityRepository transactionEntityRepository;
    private final EmailService emailService;
    private final InquiryEntityRepository inquiryEntityRepository;
    private final CustomDateFormatter customDateFormatter;
    private final ReportEntityRepository reportEntityRepository;

    /**
     * Retrieves user details DTO based on the provided username and JWT token.
     * It includes the user's subscription plan based on their Stripe transactions.
     *
     * @param username The username of the user.
     * @param jwtToken The JWT token for user authentication.
     * @return UserDetailsDTO containing user details and subscription plan.
     * @throws StripeException if there is an issue with Stripe transactions.
     */
    public UserDetailsDTO getUserDetailsDTO(String username, String jwtToken) throws StripeException {
        UserEntity userEntity = validateData.validateUserWithJWT(username, jwtToken);
        UserDetailsDTO userDetailsDTO = modelMapper.map(userEntity, UserDetailsDTO.class);
        String subPlan = DEFAULT_USER_SUBSCRIPTION;
        if (stripeService.getAllTransactionsFromEmail(userEntity.getEmail()).size() > 0) {
            String duration = stripeService.getAllTransactionsFromEmail(userEntity.getEmail()).get(0).getDuration();
            subPlan = generateSubscriptionPlanName(duration);
        }
        userDetailsDTO.setSubscription(subPlan);
        userEntity.setSubscription(subPlan);
        if (!validationUtil.isValid(userEntity) || !validationUtil.isValid(userDetailsDTO)) {
            throw new DataValidationException();
        }
        userRepository.save(userEntity);
        return userDetailsDTO;
    }

    /**
     * Updates user's biography and returns an UpdateUserBiographyDTO.
     *
     * @param username  The username of the user.
     * @param jwtToken  The JWT token for user authentication.
     * @param biography The updated user biography.
     * @return UpdateUserBiographyDTO containing the updated biography.
     * @throws DataValidationException if the provided data is not valid.
     */
    public UpdateUserBiographyDTO getUpdateUserBiographyDTO(String username, String jwtToken, String biography) {
        UserEntity userEntity = validateData.validateUserWithJWT(username, jwtToken);
        userEntity.setBiography(biography);
        if (!validationUtil.isValid(userEntity)) {
            throw new DataValidationException();
        }
        userRepository.save(userEntity);
        UpdateUserBiographyDTO updateUserBiographyDTO = new UpdateUserBiographyDTO(biography);
        if (!validationUtil.isValid(updateUserBiographyDTO)) {
            throw new DataValidationException();
        }
        return updateUserBiographyDTO;
    }


    /**
     * Generates a subscription plan name based on the provided duration.
     *
     * @param duration The duration for the subscription.
     * @return The subscription plan name.
     */
    public String generateSubscriptionPlanName(String duration) {
        return switch (duration) {
            case PLAN_DURATION_ONE -> DURATION_LEVEL_ONE;
            case PLAN_DURATION_TWO -> DURATION_LEVEL_TWO;
            case PLAN_DURATION_THREE -> DURATION_LEVEL_THREE;
            default -> DEFAULT_USER_SUBSCRIPTION;
        };
    }

    /**
     * Resets a user's password based on the provided old and new passwords.
     *
     * @param username    The username of the user.
     * @param oldPassword The old password for validation.
     * @param newPassword The new password to set.
     * @param jwtToken    The JWT token for user authentication.
     * @return A status message indicating the result of the password reset.
     */
    public String resetUserPassword(String username, String oldPassword, String newPassword, String jwtToken) {
        UserEntity userEntity = validateData.validateUserWithJWT(username, jwtToken);
        boolean passwordMatch = passwordEncoder.matches(oldPassword, userEntity.getPassword());
        if (!passwordMatch) {
            throw new AccessDeniedException();
        }
        if (newPassword.length() < 8) {
            throw new DataValidationException();
        }
        userEntity.setPassword(passwordEncoder.encode(newPassword));
        if (!validationUtil.isValid(userEntity)) {
            throw new DataValidationException();
        }
        userRepository.save(userEntity);
        return PASSWORD_MATCHES;
    }

    /**
     * Resets a user's password using a reset token and sets a new password.
     *
     * @param resetToken  The reset token for validating the password reset.
     * @param newPassword The new password to set.
     * @return A status message indicating the result of the password reset.
     */
    public String resetPasswordByToken(String resetToken, String newPassword) {
        String hashedToken = DigestUtils.sha256Hex(resetToken);
        Optional<UserEntity> userEntityByResetTokenOptional = userRepository.findUserEntityByResetToken(hashedToken);
        if (newPassword.length() < 8) {
            throw new DataValidationException();
        }
        if (userEntityByResetTokenOptional.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        UserEntity userEntity = userEntityByResetTokenOptional.get();
        if (!userEntity.getResetToken().equals(hashedToken)) {
            throw new AccessDeniedException();
        }
        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userEntity.setResetToken("");
        if (!validationUtil.isValid(userEntity)) {
            throw new DataValidationException();
        }
        userRepository.save(userEntity);
        return PASSWORD_RESET_SUCCESSFULLY;
    }


    /**
     * Retrieves all Stripe transactions for a user and ensures they are synchronized with the database.
     *
     * @param username The username of the user.
     * @param jwtToken The JWT token for user authentication.
     * @return A list of Stripe transaction DTOs.
     * @throws StripeException If there is an issue with Stripe API calls.
     */
    public List<StripeTransactionDTO> getAllUserTransactions(String username, String jwtToken) throws StripeException {
        UserEntity userEntity = validateData.validateUserWithJWT(username, jwtToken);
        List<StripeTransactionDTO> allTransactionsFromEmailDTOs = stripeService.getAllTransactionsFromEmail(userEntity.getEmail());
        List<TransactionEntity> allByUserEmail = transactionEntityRepository.findAllByUserEmail(userEntity.getEmail());
        if (allTransactionsFromEmailDTOs.size() != allByUserEmail.size()) {
            transactionEntityRepository.saveAll(allTransactionsFromEmailDTOs
                    .stream()
                    .map(transactionDTO -> {
                        TransactionEntity transactionEntity = modelMapper.map(transactionDTO, TransactionEntity.class);
                        transactionEntity.setUserEntity(userEntity);
                        return transactionEntity;
                    }).collect(Collectors.toList()));
        }

        return allByUserEmail
                .stream()
                .map(transactionEntity -> modelMapper
                        .map(transactionEntity, StripeTransactionDTO.class)).collect(Collectors.toList());
    }

    /**
     * Logs out a user by clearing the JWT token and SecurityContextHolder.
     *
     * @param username The username of the user.
     * @param jwtToken The JWT token for user authentication.
     * @return A status message indicating a successful logout.
     */
    public String logoutUser(String username, String jwtToken) {
        UserEntity userEntity = validateData.validateUserWithJWT(username, jwtToken);
        userEntity.setJwtToken(null);
        userRepository.save(userEntity);
        SecurityContextHolder.clearContext();

        return SUCCESSFUL_LOGOUT;
    }

    /**
     * Validates user data with JWT, creates and saves an inquiry entity, and sends an email.
     *
     * @param title    The title of the inquiry.
     * @param content  The content of the inquiry.
     * @param username The username associated with the inquiry.
     * @param jwtToken The JWT token for authorization.
     * @throws MessagingException If there is an issue with sending the email.
     */
    public void sendInquiryEmailAndSave(String title, String content, String username, String jwtToken) throws MessagingException {
        UserEntity userEntity = validateData.validateUserWithJWT(username, jwtToken);

        InquiryEntity inquiryEntity = new InquiryEntity();
        inquiryEntity.setDate(customDateFormatter.formatLocalDateTimeNowAsString(LocalDateTime.now()));
        inquiryEntity.setContent(content);
        inquiryEntity.setTitle(title);
        inquiryEntity.setCustomID(getRandomCustomIDNumberINQ());
        inquiryEntity.setUserEntity(userEntity);

        inquiryEntityRepository.save(inquiryEntity);

        emailService.sendInquiryEmail(inquiryEntity);
    }


    /**
     * Generates a random custom ID number for the inquiry entity.
     *
     * @return The generated custom ID number.
     */
    private String getRandomCustomIDNumberINQ() {
        int min = 10000000; // Smallest 8-digit number
        int max = 99999999; // Largest 8-digit number
        String randomNumber = INQ_PREFIX + (new Random().nextInt(max - min + 1) + min);

        if (inquiryEntityRepository.existsByCustomID(randomNumber)) {
            getRandomCustomIDNumberINQ();
        }
        return randomNumber;
    }


    /**
     * Generates a random custom ID number for the report entity.
     *
     * @return The generated custom ID number.
     */
    public String getRandomCustomIDNumberREP() {
        int min = 10000000; // Smallest 8-digit number
        int max = 99999999; // Largest 8-digit number
        String randomNumber = REPORT_PREFIX + (new Random().nextInt(max - min + 1) + min);

        if (reportEntityRepository.existsByCustomID(randomNumber)) {
            getRandomCustomIDNumberREP();
        }
        return randomNumber;
    }


    /**
     * Saves a report entity and sends an email notification.
     *
     * @param title      The title of the report.
     * @param content    The content of the report.
     * @param imgURL     The URL of the report's image.
     * @param username   The username associated with the report.
     * @param jwtToken   The JWT token for user validation.
     * @throws MessagingException If an error occurs while sending the email.
     */
    public void saveReportAndSendEmail(String title, String content, String imgURL, String username, String jwtToken) throws MessagingException {
        UserEntity userEntity = validateData.validateUserWithJWT(username, jwtToken);

        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setCustomID(getRandomCustomIDNumberREP());
        reportEntity.setTitle(title);
        reportEntity.setContent(content);
        reportEntity.setDate(customDateFormatter.formatLocalDateTimeNowAsString(LocalDateTime.now()));
        reportEntity.setImgURL(imgURL);
        reportEntity.setUserEntity(userEntity);

        reportEntityRepository.save(reportEntity);

        emailService.sendReportEmail(reportEntity,userEntity.getUsername(),userEntity.getEmail());
    }


}

