package fxibBackend.repository;

import fxibBackend.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionEntityRepository extends JpaRepository<TransactionEntity, Long> {

    List<TransactionEntity> findAllByUserEmail(String userEmail);
}
