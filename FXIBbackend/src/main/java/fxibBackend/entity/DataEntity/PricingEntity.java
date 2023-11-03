package fxibBackend.entity.DataEntity;

import fxibBackend.entity.Base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static fxibBackend.constants.InitConst.*;
import static fxibBackend.constants.TableNameConst.PRICINGS_TABLE;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = PRICINGS_TABLE)
public class PricingEntity extends BaseEntity {

    @Column(nullable = false)
    private String price;

    @Column(nullable = false)
    private String duration;

    @Column(nullable = false)
    private String firstLine = PRICING_FIRST_LINE;

    @Column(nullable = false)
    private String secondLine = PRICING_SECOND_LINE;

    @Column(nullable = false)
    private String thirdLine = PRICING_THIRD_LINE;

    @Column(nullable = false)
    private String fourthLine = PRICING_FOURTH_LINE;

    @Column(nullable = false)
    private String fifthLine = PRICING_FIFTH_LINE;

    @Column(nullable = false)
    private String sixthLine = PRICING_SIXTH_LINE;

    @Column(nullable = false)
    private String seventhLine = PRICING_SEVENTH_LINE;

    @Column(nullable = false)
    private String eighthLine = PRICING_EIGHTH_LINE;

    @Column(nullable = false)
    private String linkURL;
}
