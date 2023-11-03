package fxibBackend.dto.AdminDTOS;

import fxibBackend.entity.RoleEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class AdminUserDTO {

    @NotNull
    @Size(min = 4,max = 10)
    private String username;

    @NotNull
    @Email
    @Size(min = 5,max = 36)
    private String email;

    @NotNull
    private String subscription;

    @NotNull
    private String registrationDate;

    @NotNull
    private Set<RoleEntity> roles;
}
