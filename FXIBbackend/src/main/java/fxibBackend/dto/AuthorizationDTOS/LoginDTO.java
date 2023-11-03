package fxibBackend.dto.AuthorizationDTOS;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    @Size(min = 4, max = 10)
    @NotBlank
    private String username;

    @Size(min = 8)
    @NotBlank
    private String password;
}
