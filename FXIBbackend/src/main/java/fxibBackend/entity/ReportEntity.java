package fxibBackend.entity;


import fxibBackend.entity.Base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static fxibBackend.constants.TableNameConst.REPORTS_TABLE;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = REPORTS_TABLE)
public class ReportEntity extends BaseEntity {


    @Column(name = "custom_id")
    private String customID;

    @Column
    @Size(max = 50)
    private String title;

    @Column(columnDefinition = "TEXT")
    @Size(max = 1500)
    private String content;

    @Column
    private String date;

    @Column
    private String imgURL;

    @ManyToOne
    private UserEntity userEntity;


}
