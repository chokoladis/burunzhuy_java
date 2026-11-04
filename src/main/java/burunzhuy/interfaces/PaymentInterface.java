package burunzhuy.interfaces;

import burunzhuy.dto.external.pay.nowpayment.PaymentResponse;
import burunzhuy.dto.external.pay.nowpayment.WithdrawItemResponse;
import burunzhuy.dto.external.pay.nowpayment.WithdrawResponse;
import burunzhuy.dto.pay.PaymentMethod;
import burunzhuy.dto.pay.WithdrawRequest;

import java.math.BigDecimal;

public interface PaymentInterface {
    PaymentMethod getPaymentMethod();
    PaymentResponse deposit(BigDecimal amount, String currency);
    WithdrawResponse withdraw(WithdrawRequest request);
}
