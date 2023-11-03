package fxibBackend.repository;

import fxibBackend.entity.TopicEntity;
import fxibBackend.entity.enums.TopicEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicEntityRepository extends JpaRepository<TopicEntity, Long> {

    List<TopicEntity> findAllByOrderByIdAsc();

    TopicEntity findTopicEntityByTopicEnum(TopicEnum topicEnum);

}
