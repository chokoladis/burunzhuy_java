package burunzhuy.service.auth;

import burunzhuy.entity.security.RefreshToken;
import burunzhuy.exception.common.EntityNotFound;
import burunzhuy.helper.SecureHelper;
import burunzhuy.repository.security.RefreshTokenRepository;
import burunzhuy.service.security.JwtService;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private JwtService jwtService;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    private TokenService tokenService;

    private final String accessToken = "accessToken";
    private final String refreshToken = "refreshToken";
    private final String defaultEmail = "test@mail.ru";

    @BeforeEach
    void setUp() {
        tokenService = new TokenService(jwtService, refreshTokenRepository);
    }

//    throws JwtException
    @Test
    void getNewAccessToken_success()
            throws JwtException
    {
        try (MockedStatic<SecureHelper> mockedStatic = Mockito.mockStatic(SecureHelper.class)) {
            mockedStatic.when(() -> SecureHelper.getHashedValue(refreshToken)).thenReturn("hashed_token");

            doNothing().when(jwtService).validateToken(refreshToken, "refresh");
            when(jwtService.extractEmail(refreshToken)).thenReturn(defaultEmail);
            when(jwtService.generateToken(defaultEmail)).thenReturn(accessToken);
            when(refreshTokenRepository.findOneByToken("hashed_token")).thenReturn(this.getFillableOptRefreshToken());

            assertEquals(accessToken, tokenService.getNewAccessToken("refreshToken"));

            mockedStatic.verify(() -> SecureHelper.getHashedValue(refreshToken));
        }
    }

    @Test
    void getNewAccessToken_errorInvalidType()
            throws JwtException
    {
        doThrow(JwtException.class).when(jwtService).validateToken(refreshToken, "refresh");

        assertThrows(JwtException.class, () -> {
            tokenService.getNewAccessToken("refreshToken");
        });
    }

    @Test
    void getNewAccessToken_errorTokenNotFound()
            throws JwtException
    {
        try (MockedStatic<SecureHelper> mockedStatic = Mockito.mockStatic(SecureHelper.class)) {
            mockedStatic.when(() -> SecureHelper.getHashedValue(refreshToken)).thenReturn("hashed_token");

            doNothing().when(jwtService).validateToken(refreshToken, "refresh");
            when(jwtService.extractEmail(refreshToken)).thenReturn(defaultEmail);
            when(refreshTokenRepository.findOneByToken("hashed_token")).thenReturn(Optional.empty());

            assertThrows(EntityNotFound.class, () -> {
                tokenService.getNewAccessToken("refreshToken");
            });

            mockedStatic.verify(() -> SecureHelper.getHashedValue(refreshToken));
        }
    }

    @Test
    void revokeByUser() {
        //todo
    }

    private Optional<RefreshToken> getFillableOptRefreshToken() {
        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setIsRevoked(false);
        newRefreshToken.setExpiredAt(LocalDateTime.now().plusDays(1L));
        newRefreshToken.setToken(this.refreshToken);

        return Optional.of(newRefreshToken);
    }
}