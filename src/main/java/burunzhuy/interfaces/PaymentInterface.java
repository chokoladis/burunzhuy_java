package burunzhuy.interfaces;

import burunzhuy.dto.external.pay.nowpayment.PaymentResponse;
import burunzhuy.dto.pay.PaymentMethod;

import java.math.BigDecimal;

public interface PaymentInterface {
    PaymentMethod getPaymentMethod();
    PaymentResponse depositInnerBalance(BigDecimal amount, String currency);
    void withdrawInnerBalance();
}
