package fxibBackend.repository;

import fxibBackend.entity.QuestionEntity;
import fxibBackend.entity.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionEntityRepository extends JpaRepository<QuestionEntity, Long> {
    List<QuestionEntity> findAllByTopicEntityAndDeleted(TopicEntity topicEntity, boolean isDeleted);



}
