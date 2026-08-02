package burunzhuy.controller;

import burunzhuy.dto.auth.PasswordResetConfirmRequest;
import burunzhuy.dto.auth.PasswordResetRequest;
import burunzhuy.dto.jwt.JwtResponse;
import burunzhuy.dto.auth.LoginRequest;
import burunzhuy.dto.auth.RegisterRequest;
import burunzhuy.dto.http.ApiResponse;
import burunzhuy.entity.user.User;
import burunzhuy.exception.auth.RegisterException;
import burunzhuy.exception.common.NotificationSendException;
import burunzhuy.resource.user.UserResource;
import burunzhuy.service.auth.AuthService;
import burunzhuy.service.auth.PasswordRestoreService;
import burunzhuy.tool.Logger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.FailedLoginException;

@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final PasswordRestoreService passwordRestoreService;

    @PostMapping("register/")
    public ResponseEntity<ApiResponse<UserResource>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User userObj = authService.register(request);
            return ResponseEntity.ok(ApiResponse.ok(new UserResource(userObj)));
        } catch (RegisterException error) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(error.getMessage()));
        } catch (Throwable globalError) {
            globalError.printStackTrace();
            Logger.logToFile("auth.txt", globalError.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Ошибка выполнения кода"));
        }
    }

    @PostMapping("login/")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                        ApiResponse.ok(authService.login(request, httpRequest))
                    );
        } catch (FailedLoginException error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            ApiResponse.error(error.getMessage())
                    );
        } catch (Throwable globalError) {
            globalError.printStackTrace();
            Logger.logToFile("auth.txt", globalError.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Ошибка выполнения кода"));
        }
    }

    @PostMapping("password/send/")
    public ResponseEntity<?> passwordSendToken(
        @Valid @RequestBody PasswordResetRequest request
    ) {
        try {
            passwordRestoreService.sendToken(request);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (NotificationSendException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }

    }

    @PostMapping("password/confirm/")
    public ResponseEntity<?> passwordConfirmReset(
            @Valid @RequestBody PasswordResetConfirmRequest request
    ) {
        try {
            passwordRestoreService.restore(request);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Throwable e){
            Logger.logToFile("auth.txt", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(ApiResponse.error("Произошла непредвиденная ошибка"));
        }

    }
}
