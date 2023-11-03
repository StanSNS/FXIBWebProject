package fxibBackend.entity.DataEntity.TradingAccount;

import fxibBackend.entity.Base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static fxibBackend.constants.TableNameConst.TRADING_ACCOUNTS_TABLE;

@Entity
@Table(name = TRADING_ACCOUNTS_TABLE)
@Getter
@Setter
@NoArgsConstructor
public class TradingAccountEntity extends BaseEntity {

    @Column
    private Long responseIdentity;

    @Column
    private Long accountIdentity;

    @Column
    private Double deposits;

    @Column
    private Double profit;

    @Column
    private Double balance;

    @Column
    private Double equity;

    @Column
    private String lastUpdateDate;

    @Column
    private String creationDate;

    @Column
    private String firstTradeDate;

    @Column
    private String currency;

    @Column
    private String server;

    @OneToMany(fetch = FetchType.EAGER)
    private List<TradeEntity> trades;
}


