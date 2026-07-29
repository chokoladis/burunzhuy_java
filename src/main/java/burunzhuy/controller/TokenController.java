package burunzhuy.controller;

import burunzhuy.dto.jwt.JwtResponse;
import burunzhuy.dto.http.ApiResponse;
import burunzhuy.dto.jwt.RefreshRequest;
import burunzhuy.service.auth.TokenService;
import burunzhuy.tool.Logger;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/token/")
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("refresh/")
    public ResponseEntity<ApiResponse<String>> refresh(@RequestBody RefreshRequest refreshRequest) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                .body(
                    ApiResponse.ok(tokenService.refresh(refreshRequest.refresh_token()))
                );
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Истекло время действия токена"));
        } catch (JwtException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Некорректный refresh токен"));
        } catch (Throwable globalError) {
            globalError.printStackTrace();
            Logger.logToFile("token.txt", globalError.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Ошибка выполнения кода"));
        }
    }
}
