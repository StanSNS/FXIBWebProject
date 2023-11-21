package fxibBackend.entity;

import fxibBackend.entity.Base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static fxibBackend.constants.TableNameConst.INQUIRIES_TABLE;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = INQUIRIES_TABLE)
public class InquiryEntity extends BaseEntity {


    @NotNull
    @Column(name = "custom_id")
    private String customID;

    @NotNull
    @Column
    @Size(max = 50)
    private String title;

    @NotNull
    @Column(columnDefinition = "TEXT")
    @Size(max = 1500)
    private String content;

    @NotNull
    @Column
    private String date;

    @ManyToOne
    private UserEntity userEntity;


}
