package fxibBackend.dto.CommunityDTOS;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuestionDTO {

    @NotNull
    @Positive
    private Long id;

    @Size(max = 1500)
    @NotNull
    private String content;

    @NotNull
    private String topic;

    @NotNull
    private String date;

    @Size(min = 4, max = 10)
    @NotNull
    private String writer;

    @NotNull
    private Boolean solved;

    @Size(max = 95)
    private String userBiography;

    private String subscriptionPlan;

    @NotNull
    private List<AnswerDTO> answers = new ArrayList<>();
}
