package burunzhuy.service.auth;

import burunzhuy.entity.security.RefreshToken;
import burunzhuy.entity.user.User;
import burunzhuy.exception.auth.RefreshTokenInvalidException;
import burunzhuy.exception.common.EntityNotFound;
import burunzhuy.repository.security.RefreshTokenRepository;
import burunzhuy.service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    public String getNewAccessToken(String refresh_token)
    {
        jwtService.validateToken(refresh_token, "refresh");

        String email = jwtService.extractEmail(refresh_token);

        this.checks(refresh_token);

        return jwtService.generateToken(email);
    }

    private void checks(String refresh_token)
    {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findOneByToken(refresh_token);
        if (refreshToken.isEmpty())
            throw new EntityNotFound("Данный refresh token не был найден");

        if (refreshToken.get().getIsRevoked())
            throw new RefreshTokenInvalidException("Ваш refresh token уже не действителен");
//
        if (refreshToken.get().getExpiredAt().compareTo(LocalDateTime.now()) < 0) {
            refreshToken.get().setIsRevoked(true);
            refreshTokenRepository.save(refreshToken.get());
            throw new RefreshTokenInvalidException("Ваш refresh token уже не действителен");
        }

        // device_fingerprint, ip_address(смотреть подсеть) - для безопастности и 2фа
    }

    public void revokeByUser(User user) {
        Collection<RefreshToken> refreshTokens = refreshTokenRepository.findByUserAndIsRevokedFalse(user);
        if (refreshTokens.isEmpty())
            return;

//        refreshTokens. todo chanhe
    }
}
