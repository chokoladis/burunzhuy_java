package burunzhuy.dto.pay;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
final public class DepositRequest {
    @NotBlank(message = "Поле не должно быть пустым")
    public String amount;
    public String currency = "USDTTRC20";
}
