package fxibBackend.entity;

import fxibBackend.entity.Base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static fxibBackend.constants.TableNameConst.LOCATIONS_TABLE;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = LOCATIONS_TABLE)
public class LocationEntity extends BaseEntity {

    @Column
    private String continent;

    @Column
    private String city;

    @Column
    private String country;

    @Column
    private String ip;

    @Column
    private String countryFlagURL;

    @Column
    private String username;
}
