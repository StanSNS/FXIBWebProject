package fxibBackend.dto.UserDetailsDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocationDTO {

    @NotNull
    private String continent;

    @NotNull
    private String city;

    @NotNull
    private String country;

    @NotNull
    private String ip;

    @NotNull
    private String countryFlagURL;

    @NotNull
    private String username;
}
