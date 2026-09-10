package burunzhuy.interfaces;

import burunzhuy.dto.pay.DepositRequest;
import java.math.BigDecimal;

public interface PaymentInterface {
//    void depositInnerBalance(DepositRequest depositRequest);
    void depositInnerBalance(BigDecimal amount, String currency);
    void withdrawInnerBalance();
}
