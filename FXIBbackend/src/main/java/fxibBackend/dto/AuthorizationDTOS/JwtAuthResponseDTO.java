package fxibBackend.dto.AuthorizationDTOS;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class JwtAuthResponseDTO {

    @NotNull
    private String accessToken;

    private String tokenType = "Bearer";

    @NotNull
    private Set<String> role;

    @NotNull
    private String email;
}
