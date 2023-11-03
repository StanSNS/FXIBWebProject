package fxibBackend.repository;

import fxibBackend.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationEntityRepository extends JpaRepository<LocationEntity, Long> {




}
