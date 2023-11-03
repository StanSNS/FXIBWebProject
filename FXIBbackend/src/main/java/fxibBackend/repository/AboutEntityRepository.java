package fxibBackend.repository;

import fxibBackend.entity.DataEntity.AboutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AboutEntityRepository extends JpaRepository<AboutEntity, Long> {
}
