package fxibBackend.repository;

import fxibBackend.entity.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerEntityRepository extends JpaRepository<AnswerEntity, Long> {
    Optional<AnswerEntity> findByIdAndDeleted(Long id, boolean deleted);
}