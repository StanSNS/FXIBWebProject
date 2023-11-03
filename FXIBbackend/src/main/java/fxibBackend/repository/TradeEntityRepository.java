package fxibBackend.repository;

import fxibBackend.entity.DataEntity.TradingAccount.TradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeEntityRepository extends JpaRepository<TradeEntity,Long> {




}
