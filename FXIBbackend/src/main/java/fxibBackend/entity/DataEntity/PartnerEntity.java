package fxibBackend.entity.DataEntity;


import fxibBackend.entity.Base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static fxibBackend.constants.TableNameConst.PARTNERS_TABLE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = PARTNERS_TABLE)
public class PartnerEntity extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String firstLine;

    @Column(nullable = false)
    private String secondLine;

    @Column(nullable = false)
    private String thirdLine;

    @Column(nullable = false)
    private String fourthLine;

    @Column(nullable = false)
    private String fifthLine;

    @Column(nullable = false)
    private String sixthLine;

    @Column(nullable = false)
    private String seventhLine;

    @Column(nullable = false)
    private String linkURL;
}
