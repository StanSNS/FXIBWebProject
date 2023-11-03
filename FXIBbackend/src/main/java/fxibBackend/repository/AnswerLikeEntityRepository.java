package fxibBackend.repository;

import fxibBackend.entity.AnswerLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerLikeEntityRepository extends JpaRepository<AnswerLikeEntity, Long> {

    Optional<AnswerLikeEntity> getByAnswerID(Long answerID);

    Optional<AnswerLikeEntity> getAnswerLikeEntityByAnswerIDAndUsernameAndDeleted(Long id, String username, boolean deleted);

    Optional<AnswerLikeEntity> findByAnswerID(Long answerID);

    boolean existsByAnswerIDAndUsername(Long answerID, String username);

}
