package fxibBackend.dto.InitDTOS;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PartnerDTO {

    @NotNull
    private String title;

    @NotNull
    private String firstLine;

    @NotNull
    private String secondLine;

    @NotNull
    private String thirdLine;

    @NotNull
    private String fourthLine;

    @NotNull
    private String fifthLine;

    @NotNull
    private String sixthLine;

    @NotNull
    private String seventhLine;

    @NotNull
    private String linkURL;
}
