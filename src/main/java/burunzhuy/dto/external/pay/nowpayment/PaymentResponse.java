package burunzhuy.dto.external.pay.nowpayment;

import burunzhuy.enums.pay.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PaymentResponse {
    private String payment_id;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
    private Status payment_status;
    private String pay_address; // нужно сюда перевести
    private BigDecimal price_amount;
    private String price_currency;
    private BigDecimal pay_amount;
    private BigDecimal amount_received;
    private String pay_currency;
    private String order_id;
    private String order_description;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private LocalDateTime expiration_estimate_date;
    private String success;
//            "purchase_id":"5620097793",
//            "smart_contract":null,
//            "network":"trx",
//            "type":"crypto2crypto",
}
