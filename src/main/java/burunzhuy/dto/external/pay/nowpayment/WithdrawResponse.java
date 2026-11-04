package burunzhuy.dto.external.pay.nowpayment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class WithdrawResponse {
    private List<WithdrawItemResponse> withdrawals;
}
