package burunzhuy.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetConfirmRequest {

    @NotBlank
    @Size(min = 64, max = 64)
    private String token;

    @NotBlank
    @Size(min = 8, max = 64, message = "Введите от 8 до 64 символов")
    private String password;
}
