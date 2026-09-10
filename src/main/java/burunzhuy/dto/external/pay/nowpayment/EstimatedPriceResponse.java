package burunzhuy.dto.external.pay.nowpayment;

import java.math.BigDecimal;

public record EstimatedPriceResponse(
    String currency_from,
    BigDecimal amount_from,
    String currency_to,
    BigDecimal estimated_amount
) {
}
