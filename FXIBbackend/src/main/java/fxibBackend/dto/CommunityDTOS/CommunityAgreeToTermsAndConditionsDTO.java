package fxibBackend.dto.CommunityDTOS;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommunityAgreeToTermsAndConditionsDTO {

    @Size(min = 4, max = 10)
    @NotNull
    private String username;
    private boolean agreedToTerms;
}
