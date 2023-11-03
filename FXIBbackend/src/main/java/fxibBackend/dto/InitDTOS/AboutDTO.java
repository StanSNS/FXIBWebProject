package fxibBackend.dto.InitDTOS;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AboutDTO {

    @NotNull
    private String title;

    @NotNull
    private String description;
}
