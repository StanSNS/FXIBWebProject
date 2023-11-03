package fxibBackend.dto.AdminDTOS;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RolesAdminDTO {

    @Positive
    private Long id;

    @NotNull
    @Size(min = 4, max = 6)
    private String name;
}
