package fxibBackend.entity;

import fxibBackend.entity.Base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static fxibBackend.constants.TableNameConst.USERS_TABLE;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = USERS_TABLE)
public class UserEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    @Size(min = 4, max = 10)
    @NotNull
    private String username;

    @Column(nullable = false, unique = true)
    @Email
    @Size(min = 5, max = 36)
    @NotNull
    private String email;

    @Column(nullable = false, unique = true)
    @Size(min = 8)
    @NotNull
    private String password;

    @Column
    @NotNull
    private String subscription;

    @Column
    @NotNull
    private String registrationDate;

    @Column
    @Size(max = 95)
    @NotNull
    private String biography;

    @Column
    private String resetToken;

    @Column
    @NotNull
    private Boolean agreedToTerms;

    @Column
    private String jwtToken;

    @Column
    private Integer numberOfLogins;

    @Column
    private Integer twoFactorLoginCode;

    @OneToMany(fetch = FetchType.EAGER)
    private List<QuestionEntity> questions;

    @ManyToOne(cascade = CascadeType.ALL)
    private AnswerLikeEntity likedAnswer;

    @OneToOne
    private LocationEntity locationEntity;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<RoleEntity> roles;
}
