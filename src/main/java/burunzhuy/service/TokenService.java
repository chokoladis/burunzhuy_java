package burunzhuy.service;

import burunzhuy.dto.jwt.JwtResponse;
import burunzhuy.service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtService jwtService;

    public JwtResponse refresh(String refresh_token)
    {
        jwtService.validateToken(refresh_token, "refresh");

        return jwtService.refreshToken(refresh_token);
    }
}
