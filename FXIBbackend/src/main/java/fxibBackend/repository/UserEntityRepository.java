package fxibBackend.repository;

import fxibBackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByTwoFactorLoginCode(int code);
    Optional<UserEntity> findUserEntityByResetToken(String resetToken);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    Boolean existsByUsername(String username);
}
