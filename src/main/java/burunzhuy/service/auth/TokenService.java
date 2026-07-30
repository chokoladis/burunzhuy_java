package burunzhuy.service.auth;

import burunzhuy.service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtService jwtService;

    public String refresh(String refresh_token)
    {
        jwtService.validateToken(refresh_token, "refresh");
        // todo save to db

        return jwtService.getNewAccessTokenByRefresh(refresh_token);

    }
}
