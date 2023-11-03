package fxibBackend.repository;

import fxibBackend.entity.DataEntity.TradingAccount.TradingAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradingAccountEntityRepository extends JpaRepository<TradingAccountEntity, Long> {
}
