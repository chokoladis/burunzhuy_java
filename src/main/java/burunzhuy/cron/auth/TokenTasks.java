package burunzhuy.cron.auth;

import burunzhuy.repository.security.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
public class TokenTasks {
    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "0 0 2 * * *")
    public void cleanOld()
    {

    }
}
