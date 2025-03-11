package org.epde.ingrecipe.auth.repository;

import org.epde.ingrecipe.auth.model.TokenBlackList;
import org.epde.ingrecipe.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface TokenBlackListRepository extends JpaRepository<TokenBlackList, Long> {

    boolean existsByTokenAndInvalidatedAtIsNotNull(String token);

    TokenBlackList findByToken(String token);

    @Modifying
    @Transactional
    @Query("UPDATE TokenBlackList t SET t.invalidatedAt = :invalidatedAt WHERE t.token = :token AND t.user = :user")
    void invalidateToken(@Param("token") String token, @Param("user") Users user, @Param("invalidatedAt") LocalDateTime invalidatedAt);

    @Query("SELECT t FROM TokenBlackList t WHERE t.user = :user AND t.invalidatedAt IS NULL")
    TokenBlackList findByUserAndInvalidatedAtIsNull(@Param("user") Users user);
}
