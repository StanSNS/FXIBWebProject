package fxibBackend.entity;

import fxibBackend.entity.Base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static fxibBackend.constants.TableNameConst.LIKED_ANSWERS_TABLE;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = LIKED_ANSWERS_TABLE)
public class AnswerLikeEntity extends BaseEntity {

    @Column
    @Size(min = 4, max = 10)
    @NotNull
    private String username;

    @Column
    @Positive
    @NotNull
    private Long answerID;

    @Column
    @NotNull
    private Boolean deleted;
}
