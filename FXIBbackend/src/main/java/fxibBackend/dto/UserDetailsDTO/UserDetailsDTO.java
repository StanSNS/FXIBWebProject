package fxibBackend.dto.UserDetailsDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class UserDetailsDTO {

    @Size(min = 4, max = 10)
    @NotNull
    private String username;

    @Email
    @Size(min = 5, max = 36)
    @NotNull
    private String email;

    @NotNull
    private String registrationDate;

    private String subscription;

    @Size(max = 95)
    @NotNull
    private String biography;
}
