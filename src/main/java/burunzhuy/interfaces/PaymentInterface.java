package burunzhuy.interfaces;

import burunzhuy.dto.external.pay.nowpayment.PaymentResponse;
import burunzhuy.dto.pay.DepositRequest;
import java.math.BigDecimal;

public interface PaymentInterface {
//    void depositInnerBalance(DepositRequest depositRequest);
    PaymentResponse depositInnerBalance(BigDecimal amount, String currency);
    void withdrawInnerBalance();
}
