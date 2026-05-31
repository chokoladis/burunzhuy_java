package burunzhuy.controller;

import burunzhuy.dto.auth.RegisterRequest;
import burunzhuy.entity.User;
import burunzhuy.exception.auth.RegisterException;
import burunzhuy.resource.user.UserResource;
import burunzhuy.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // todo response -> ApiResponse<UserResource>
    // todo в любом случае отдавать json, сделать логгер
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request){
        try {
            User userObj = authService.register(request);
            return ResponseEntity.ok(new UserResource(userObj));
        } catch (RegisterException error) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error.getMessage());
        } catch (Throwable globalError) {
//            todo to log -> globalError
            return ResponseEntity.internalServerError().body("Ошибка выполнения кода");
        }
    }
}
