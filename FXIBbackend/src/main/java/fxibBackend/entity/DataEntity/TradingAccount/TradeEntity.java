package fxibBackend.entity.DataEntity.TradingAccount;

import fxibBackend.entity.Base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import static fxibBackend.constants.TableNameConst.TRADES_TABLE;


@Entity
@Table(name = TRADES_TABLE)
@Getter
@Setter
public class TradeEntity extends BaseEntity {

    @Column
    private String openTime;

    @Column
    private String closeTime;

    @Column
    private String symbol;

    @Column
    private String action;

    @Column
    private double pips;

    @Column
    private double profit;

    @Column
    private double commission;
}
