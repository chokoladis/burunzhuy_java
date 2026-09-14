package burunzhuy.dto.external.pay.nowpayment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayoutRequest {
    private PayoutWithdrawalRequest[] withdrawals;
}
