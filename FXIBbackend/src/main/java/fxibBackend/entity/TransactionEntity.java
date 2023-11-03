package fxibBackend.entity;


import fxibBackend.entity.Base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static fxibBackend.constants.TableNameConst.TRANSACTIONS_TABLE;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = TRANSACTIONS_TABLE)
public class TransactionEntity extends BaseEntity {

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private String billingDate;

    @Column(nullable = false)
    private String duration;

    @Column(nullable = false)
    private String endOfBillingDate;

    @Column(nullable = false)
    private String amount;

    @Column(nullable = false)
    private String card;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String receipt;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    private UserEntity userEntity;
}
