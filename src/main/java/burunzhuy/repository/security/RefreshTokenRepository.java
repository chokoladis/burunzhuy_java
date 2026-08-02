package burunzhuy.repository.security;

import burunzhuy.entity.security.RefreshToken;
import burunzhuy.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findOneByToken(String token);
    Collection<RefreshToken> findByUserAndIsRevokedFalse(User user);
    Collection<RefreshToken> findByIsRevokedFalseAndExpiredAtBefore(LocalDateTime expiredAt);
    Collection<RefreshToken> findByCreatedAtBefore(LocalDateTime createdAt);
}
