package fxibBackend.entity;

import fxibBackend.entity.Base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static fxibBackend.constants.TableNameConst.QUESTIONS_TABLE;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = QUESTIONS_TABLE)
public class QuestionEntity extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    @Size(max = 1500)
    @NotNull
    private String content;

    @Column
    @NotNull
    private String date;

    @Column
    @NotNull
    @Size(min = 4, max = 10)
    private String writer;

    @Column
    @NotNull
    private Boolean solved;

    @Column
    @NotNull
    private Boolean deleted;

    @ManyToOne
    private TopicEntity topicEntity;

    @OneToMany(fetch = FetchType.EAGER)
    private List<AnswerEntity> answers;
}
