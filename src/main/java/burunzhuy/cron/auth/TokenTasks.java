package burunzhuy.cron.auth;

import burunzhuy.entity.security.RefreshToken;
import burunzhuy.repository.security.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class TokenTasks {
    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "0 0 2 * * *")
    public void revokeExpired()
    {
        Collection<RefreshToken> refreshTokens = refreshTokenRepository.findByIsRevokedFalseAndExpiredAtBefore(LocalDateTime.now());
        if (refreshTokens.isEmpty())
            return;

        for(RefreshToken token : refreshTokens) {
            token.setIsRevoked(true);
        }

        refreshTokenRepository.saveAllAndFlush(refreshTokens);
    }

    @Scheduled(cron = "0 5 2 * * *")
    public void deleteOld()
    {
        Collection<RefreshToken> refreshTokens = refreshTokenRepository.findByCreatedAtBefore(LocalDateTime.now().minusDays(30L));
        if (refreshTokens.isEmpty())
            return;

        refreshTokenRepository.deleteAllInBatch(refreshTokens);
    }
}
