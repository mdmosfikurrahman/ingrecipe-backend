package org.epde.ingrecipe.auth.model;

import jakarta.persistence.*;
import lombok.*;
import org.epde.ingrecipe.user.model.Users;

import java.time.LocalDateTime;

@Entity
@Table(name = "token_blacklist")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenBlackList {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_seq")
    @SequenceGenerator(name = "token_seq", sequenceName = "token_blacklist_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private Users user;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime invalidatedAt;
}
