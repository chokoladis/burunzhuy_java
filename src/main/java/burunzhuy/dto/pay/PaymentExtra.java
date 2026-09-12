package burunzhuy.dto.pay;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
final public class PaymentExtra {
    private String url;
    private String pay_address;
    private BigDecimal pay_amount;
}
