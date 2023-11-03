package fxibBackend.dto.AuthorizationDTOS;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterDTO {

    @Size(min = 4, max = 10)
    @NotNull
    private String username;

    @Email
    @Size(min = 5, max = 36)
    @NotNull
    private String email;

    @Size(min = 8)
    @NotNull
    private String password;
}
