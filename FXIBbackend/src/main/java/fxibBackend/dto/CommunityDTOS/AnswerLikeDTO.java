package fxibBackend.dto.CommunityDTOS;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnswerLikeDTO {


    @Column
    @Size(min = 4, max = 10)
    @NotNull
    private String username;


    @Column
    @Positive
    @NotNull
    private Long answerID;
}
