package fxibBackend.dto.TradingAccountsDTOS.AccountPage;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class TradeDTO {

    @NotNull
    private String openTime;

    @NotNull
    private String closeTime;

    @NotNull
    private String symbol;

    @NotNull
    private String action;

    @NotNull
    private double pips;

    @NotNull
    private double profit;

    @NotNull
    private double commission;
}
