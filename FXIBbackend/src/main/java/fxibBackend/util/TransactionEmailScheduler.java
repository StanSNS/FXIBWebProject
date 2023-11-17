package fxibBackend.util;

import fxibBackend.entity.TransactionEntity;
import fxibBackend.entity.UserEntity;
import fxibBackend.exception.ResourceNotFoundException;
import fxibBackend.repository.TransactionEntityRepository;
import fxibBackend.repository.UserEntityRepository;
import fxibBackend.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TransactionEmailScheduler {

    private final TransactionEntityRepository transactionEntityRepository;
    private final EmailService emailService;
    private final UserEntityRepository userEntityRepository;

    @Scheduled(fixedDelay = 180000) //3 Minutes
    public void transactionsEmailSendScheduler() throws MessagingException {
        for (TransactionEntity transactionEmailToSend : transactionEntityRepository.findAllByEmailSent(false)) {
            Optional<UserEntity> byEmail = userEntityRepository.findByEmail(transactionEmailToSend.getUserEmail());
            if (byEmail.isEmpty()) {
                throw new ResourceNotFoundException();
            }
            UserEntity userEntity = byEmail.get();
            emailService.sendSuccessfulPaymentEmail(transactionEmailToSend, userEntity.getUsername());
            transactionEmailToSend.setEmailSent(true);
            transactionEntityRepository.save(transactionEmailToSend);
        }
    }


}
