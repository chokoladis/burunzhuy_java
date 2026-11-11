package burunzhuy.interfaces;

import burunzhuy.dto.external.pay.nowpayment.CurrencyResponse;
import burunzhuy.dto.external.pay.nowpayment.PaymentResponse;
import burunzhuy.dto.external.pay.nowpayment.WithdrawResponse;
import burunzhuy.dto.pay.WithdrawRequest;

import java.math.BigDecimal;
import java.util.Optional;

public interface PaymentInterface {

    PaymentResponse deposit(BigDecimal amount, String currency);
    WithdrawResponse withdraw(WithdrawRequest request);

    Optional<CurrencyResponse> getCurrencies(); // todo ?
    String getCode();
}
