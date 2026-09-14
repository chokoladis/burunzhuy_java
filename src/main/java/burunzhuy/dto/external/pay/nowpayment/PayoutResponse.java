package burunzhuy.dto.external.pay.nowpayment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayoutResponse {
    private String id;
    private PayoutWithdrawalResponse[] withdrawals;
}
