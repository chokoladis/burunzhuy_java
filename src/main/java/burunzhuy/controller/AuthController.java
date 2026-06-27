package burunzhuy.controller;

import burunzhuy.dto.auth.LoginRequest;
import burunzhuy.dto.auth.RegisterRequest;
import burunzhuy.dto.http.ApiResponse;
import burunzhuy.entity.User;
import burunzhuy.exception.auth.RegisterException;
import burunzhuy.resource.user.UserResource;
import burunzhuy.service.AuthService;
import burunzhuy.tool.Logger;
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

    @PostMapping("register")
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

    @PostMapping("login")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody LoginRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            ApiResponse.ok(authService.login(request))
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
}
