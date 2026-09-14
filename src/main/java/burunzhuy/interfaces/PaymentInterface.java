package burunzhuy.interfaces;

import burunzhuy.dto.external.pay.nowpayment.PaymentResponse;
import burunzhuy.dto.external.pay.nowpayment.PayoutResponse;
import burunzhuy.dto.external.pay.nowpayment.PayoutWithdrawalRequest;
import burunzhuy.dto.pay.PaymentMethod;

import java.math.BigDecimal;

public interface PaymentInterface {
    PaymentMethod getPaymentMethod();
    PaymentResponse deposit(BigDecimal amount, String currency);
    PayoutResponse payout(PayoutWithdrawalRequest request);
}
