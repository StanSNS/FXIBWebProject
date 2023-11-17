package UtilTest;

import fxibBackend.entity.TransactionEntity;
import fxibBackend.entity.UserEntity;
import fxibBackend.exception.ResourceNotFoundException;
import fxibBackend.repository.TransactionEntityRepository;
import fxibBackend.repository.UserEntityRepository;
import fxibBackend.service.EmailService;
import fxibBackend.util.TransactionEmailScheduler;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

class TransactionEmailSchedulerTest {

    @Mock
    private TransactionEntityRepository transactionEntityRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private UserEntityRepository userEntityRepository;

    private TransactionEmailScheduler scheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        scheduler = new TransactionEmailScheduler(transactionEntityRepository, emailService, userEntityRepository);
    }

    @Test
    void transactionsEmailSendScheduler() throws MessagingException {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setUserEmail("test@example.com");
        when(transactionEntityRepository.findAllByEmailSent(false)).thenReturn(Collections.singletonList(transactionEntity));

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        when(userEntityRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userEntity));

        scheduler.transactionsEmailSendScheduler();

        verify(emailService, times(1)).sendSuccessfulPaymentEmail(eq(transactionEntity), eq("testUser"));
        verify(transactionEntityRepository, times(1)).save(transactionEntity);
    }

    @Test
    void transactionsEmailSendSchedulerUserNotFound() {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setUserEmail("test@example.com");
        when(transactionEntityRepository.findAllByEmailSent(false)).thenReturn(Collections.singletonList(transactionEntity));

        when(userEntityRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> scheduler.transactionsEmailSendScheduler());
    }
}
