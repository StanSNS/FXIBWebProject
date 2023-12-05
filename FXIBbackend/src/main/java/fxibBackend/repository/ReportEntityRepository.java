package fxibBackend.repository;

import fxibBackend.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportEntityRepository extends JpaRepository<ReportEntity, Long> {

    boolean existsByCustomID(String customID);

    List<ReportEntity> findAllByUserEntity_Id(Long id);
}
