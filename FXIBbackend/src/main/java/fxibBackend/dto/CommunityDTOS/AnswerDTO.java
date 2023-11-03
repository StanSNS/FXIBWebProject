package fxibBackend.dto.CommunityDTOS;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnswerDTO {

    @NotNull
    @Positive
    private Long id;

    @Size(max = 1500)
    @NotNull
    private String content;

    @NotNull
    @Min(0)
    private Long voteCount;

    @Size(min = 4, max = 10)
    @NotNull
    private String writer;

    @NotNull
    private String date;


    private String userSubscriptionPlan;

    @Size(max = 95)

    private String userBiography;

    @NotNull
    private Boolean liked = false;
}
