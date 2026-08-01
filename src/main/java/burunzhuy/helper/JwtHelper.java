package burunzhuy.helper;

import burunzhuy.tool.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class JwtHelper {
    @Value("${jwt.lifetime_token}")
    private long lifetime_token;

    @Value("${jwt.lifetime_refresh_token}")
    private long lifetime_refresh_token;

    public LocalDateTime getExpiredAtRefreshToken()
    {
        return LocalDateTime.now().plusSeconds(lifetime_refresh_token);
    }
}
