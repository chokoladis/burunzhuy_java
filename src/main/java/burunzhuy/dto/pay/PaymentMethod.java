package burunzhuy.dto.pay;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentMethod {
    @NotBlank
    private String code;
    @NotBlank
    private String description;
    @NotBlank
    private String img;
}
