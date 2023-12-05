package fxibBackend.dto.AdminDTOS;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportDTO {

    @NotNull
    private String customID;

    @NotNull
    private String date;

    @NotNull
    @Size(max = 50)
    private String title;

    @NotNull
    private String imgURL;
}
