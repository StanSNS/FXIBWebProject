package fxibBackend.repository;

import fxibBackend.entity.InquiryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryEntityRepository extends JpaRepository<InquiryEntity, Long> {

    boolean existsByCustomID(String customID);

}
