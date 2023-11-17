package fxibBackend.repository;

import fxibBackend.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionEntityRepository extends JpaRepository<TransactionEntity, Long> {

    List<TransactionEntity> findAllByUserEmail(String userEmail);
    List<TransactionEntity> findAllByEmailSent(boolean isEmailSent);

    boolean existsByAmountAndBillingDateAndCardAndDurationAndEndOfBillingDateAndUserEmail(String amount, String billingDate, String card, String duration, String endOfBilling, String userEmail);

}
