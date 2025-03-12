package org.epde.ingrecipe.auth.token.repository;

import org.epde.ingrecipe.auth.token.model.RevokedToken;
import org.epde.ingrecipe.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface RevokedTokenRepository extends JpaRepository<RevokedToken, Long> {

    boolean existsByTokenAndInvalidatedAtIsNotNull(String token);

    RevokedToken findByToken(String token);

    @Modifying
    @Transactional
    @Query("UPDATE RevokedToken t SET t.invalidatedAt = :invalidatedAt WHERE t.token = :token AND t.user = :user")
    void invalidateToken(@Param("token") String token, @Param("user") Users user, @Param("invalidatedAt") LocalDateTime invalidatedAt);

    @Query("SELECT t FROM RevokedToken t WHERE t.user = :user AND t.invalidatedAt IS NULL")
    RevokedToken findByUserAndInvalidatedAtIsNull(@Param("user") Users user);
}
