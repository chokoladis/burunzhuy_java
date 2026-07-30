package burunzhuy.service.security;

import burunzhuy.dto.jwt.JwtResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
final public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime_token}")
    private long lifetime_token;

    @Value("${jwt.lifetime_refresh_token}")
    private long lifetime_refresh_token;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .claim("type", "access")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + lifetime_token))
                .signWith(getKey())
                .compact();
    }

    public String generateRefreshToken(String email) {
        return Jwts.builder()
            .subject(email)
            .claim("type", "refresh")
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + lifetime_refresh_token))
            .signWith(getKey())
            .compact();
    }

    public JwtResponse getNewTokens(String email) {
        return new JwtResponse(
            this.generateToken(email),
            this.generateRefreshToken(email)
        );
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    public void validateToken(
        String token, String typeToken
    ) throws JwtException {
        Claims claims = getClaims(token);
        boolean isDateCorrect = claims
                .getExpiration()
                .after(new Date());
        var type = claims.get("type");

        if (!typeToken.equals(type)) {
            throw new JwtException("Некорректный тип токена");
        }
        if (!isDateCorrect) {
            throw new ExpiredJwtException(null, claims, "Действие токена авторизации истекло");
        }
    }

    public String getNewAccessTokenByRefresh(String refresh_token) {
        String email = extractEmail(refresh_token);

        return this.generateToken(email);
    }
}
