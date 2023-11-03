package fxibBackend.inits.TradingAccountJSONObjects;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MyFxBookAccountJSON {

    @NotNull
    private long id;

    @NotNull
    private long accountId;

    @NotNull
    private double deposits;

    @NotNull
    private double profit;

    @NotNull
    private double balance;

    @NotNull
    private double equity;

    @NotNull
    private String lastUpdateDate;

    @NotNull
    private String creationDate;

    @NotNull
    private String firstTradeDate;

    @NotNull
    private String currency;

    @NotNull
    private MyFxBookServerJSON server;

    @NotNull
    private TradeHistoryJSON tradingHistory;
}
