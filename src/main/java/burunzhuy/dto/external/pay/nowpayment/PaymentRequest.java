package burunzhuy.dto.external.pay.nowpayment;

import java.math.BigDecimal;
import java.math.BigInteger;

public record PaymentRequest(
    BigDecimal price_amount,
    String price_currency,
    BigDecimal pay_amount,
    String pay_currency
//    String ipn_callback_url,
//    BigInteger order_id
) {
}
