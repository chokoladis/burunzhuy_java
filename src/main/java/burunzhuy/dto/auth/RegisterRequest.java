package burunzhuy.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank(message = "Поле не должно быть пустым")
    @Email
    private String email;

    @NotBlank(message = "Поле не должно быть пустым")
    @Size(min = 8, max = 64, message = "Введите от 8 до 64 символов")
    private String password;

    @Size(max = 60)
    @Pattern(regexp = "^[а-яА-ЯёЁ]+$", message = "Поле принимает только кирилицу")
    private String name;
    @Size(max = 60)
    @Pattern(regexp = "^[а-яА-ЯёЁ]+$", message = "Поле принимает только кирилицу")
    private String secondName;
    @Size(max = 60)
    @Pattern(regexp = "^[а-яА-ЯёЁ]+$", message = "Поле принимает только кирилицу")
    private String lastName;

    @Size(min = 6, max = 15, message = "Телефон состоит от 6 до 15 символов")
    private String phone;
}
