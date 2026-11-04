package burunzhuy.dto.external.pay.nowpayment;

import burunzhuy.dto.pay.WithdrawRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class WithdrawalRequest {
    private List<WithdrawRequest> requests;
}
