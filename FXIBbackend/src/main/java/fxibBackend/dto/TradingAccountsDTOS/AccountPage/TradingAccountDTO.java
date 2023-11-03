package fxibBackend.dto.TradingAccountsDTOS.AccountPage;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class TradingAccountDTO {

    @NotNull
    private Long responseIdentity;

    @NotNull
    private Long accountIdentity;

    @NotNull
    private Double deposits;

    @NotNull
    private Double profit;

    @NotNull
    private Double balance;

    @NotNull
    private Double equity;

    @NotNull
    private String lastUpdateDate;

    @NotNull
    private String creationDate;

    @NotNull
    private String firstTradeDate;

    @NotNull
    private String currency;

    @NotNull
    private String server;

    @NotNull
    private List<TradeDTO> trades;
}
