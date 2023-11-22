package fxibBackend.repository;

import fxibBackend.entity.InquiryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryEntityRepository extends JpaRepository<InquiryEntity, Long> {

    boolean existsByCustomID(String customID);

    List<InquiryEntity> findAllByUserEntity_Id(Long id);

}
