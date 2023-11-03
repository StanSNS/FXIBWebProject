package fxibBackend.entity;


import fxibBackend.entity.Base.BaseEntity;
import fxibBackend.entity.enums.TopicEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static fxibBackend.constants.TableNameConst.TOPICS_TABLE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = TOPICS_TABLE)
public class TopicEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    @NotNull
    private TopicEnum topicEnum;
}
