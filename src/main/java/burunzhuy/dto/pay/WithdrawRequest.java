package burunzhuy.dto.pay;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
final public class WithdrawRequest {
    @NotBlank(message = "Поле не должно быть пустым")
    public String address;
    @NotBlank(message = "Поле не должно быть пустым")
    public BigDecimal amount;
    public String currency = "USDTTRC20";
    public String extra = null;
}
