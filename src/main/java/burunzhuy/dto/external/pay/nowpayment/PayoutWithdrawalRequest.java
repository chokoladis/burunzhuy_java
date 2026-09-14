package burunzhuy.dto.external.pay.nowpayment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PayoutWithdrawalRequest {
    @NotBlank
    private String address;
    @NotNull
    @Min(20)
    private BigDecimal amount;
    @NotBlank
    private String currency;
    private BigDecimal fiat_amount;
    private String fiat_currency;
}
