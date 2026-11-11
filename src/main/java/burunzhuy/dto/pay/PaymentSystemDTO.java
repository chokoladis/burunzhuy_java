package burunzhuy.dto.pay;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentSystemDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String code;
    private String description;
    @NotBlank
    private String img;
}
