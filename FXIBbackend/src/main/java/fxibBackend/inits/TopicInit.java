package fxibBackend.inits;

import fxibBackend.entity.TopicEntity;
import fxibBackend.entity.enums.TopicEnum;
import fxibBackend.repository.TopicEntityRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TopicInit {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final TopicEntityRepository topicEntityRepository;

    /**
     * Initializes topics if no records exist in the TopicEntityRepository.
     * Creates and saves default TopicEntity records based on TopicEnum values.
     */
    @PostConstruct
    public void topicInit() {
        // Check if there are no existing TopicEntity records
        if (topicEntityRepository.count() == 0) {
            // Loop through the TopicEnum values and create/save TopicEntity records for each topic
            for (TopicEnum topicEnum : TopicEnum.values()) {
                TopicEntity topicEntity = new TopicEntity(topicEnum);
                topicEntityRepository.save(topicEntity);
            }
        }
    }

}
