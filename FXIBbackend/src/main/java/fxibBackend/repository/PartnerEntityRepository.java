package fxibBackend.repository;

import fxibBackend.entity.DataEntity.PartnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerEntityRepository extends JpaRepository<PartnerEntity, Long> {
}
