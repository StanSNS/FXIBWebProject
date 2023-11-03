package fxibBackend.dto.TradingAccountsDTOS.Footer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TradingAccountFooterDTO {

    @Positive
    private Long id;

    @NotNull
    private Double gain;
}
