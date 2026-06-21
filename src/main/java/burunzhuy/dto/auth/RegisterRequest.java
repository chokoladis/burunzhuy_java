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

    @Size(max = 100)
    @Pattern(regexp = "^[а-яА-ЯёЁ]+$", message = "Поле принимает только кирилицу")
    private String name;
    @Size(max = 100)
    @Pattern(regexp = "^[а-яА-ЯёЁ]+$", message = "Поле принимает только кирилицу")
    private String secondName;
    @Size(max = 100)
    @Pattern(regexp = "^[а-яА-ЯёЁ]+$", message = "Поле принимает только кирилицу")
    private String lastName;

    @Size(min = 11, message = "Телефон состоит из 11 символов")
    private String phone;
}
