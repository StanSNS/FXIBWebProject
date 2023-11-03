package fxibBackend.entity;

import fxibBackend.entity.Base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static fxibBackend.constants.TableNameConst.ANSWERS_TABLE;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = ANSWERS_TABLE)
public class AnswerEntity extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    @Size(max = 1500)
    @NotNull
    private String content;

    @Column
    @Size(min = 4, max = 10)
    @NotNull
    private String writer;

    @Column
    @NotNull
    private String date;

    @Column
    @NotNull
    @Min(0)
    private Long voteCount = 0L;

    @Column
    @NotNull
    private Boolean deleted;
}
