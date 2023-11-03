package fxibBackend.repository;

import fxibBackend.entity.DataEntity.PricingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PricingEntityRepository extends JpaRepository<PricingEntity, Long> {
}
