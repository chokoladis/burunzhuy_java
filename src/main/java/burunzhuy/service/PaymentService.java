package burunzhuy.service;

import burunzhuy.dto.external.pay.nowpayment.PayoutResponse;
import burunzhuy.dto.external.pay.nowpayment.PayoutWithdrawalRequest;
import burunzhuy.dto.pay.PaymentExtra;
import burunzhuy.entity.pay.PaymentInvoice;
import burunzhuy.enums.pay.TypeTransaction;
import burunzhuy.interfaces.PaymentInterface;
import burunzhuy.repository.pay.PaymentInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentInterface paymentHandler;
    private final WalletService walletService;
    private final PaymentInvoiceRepository paymentInvoiceRepository;

//    todo transaction
//    @Transactional
    public void createDeposit(BigDecimal amount, String currency) {
        var walletHistory = walletService.createTransaction(amount, currency, TypeTransaction.DEPOSIT);

        // todo pre transaction
        var paymentResponse = paymentHandler.deposit(amount, currency);

        var paymentInvoice = new PaymentInvoice();
        paymentInvoice.setAmount(amount);
        paymentInvoice.setWalletHistory(walletHistory);
        paymentInvoice.setExternalId(paymentResponse.getPayment_id());
        paymentInvoice.setStatus(paymentResponse.getPayment_status());
        paymentInvoice.setExpirationAt(paymentResponse.getExpiration_estimate_date());

        var extra = new PaymentExtra();
        extra.setPayAmount(paymentResponse.getPay_amount());
        extra.setPayAddress(paymentResponse.getPay_address());

        paymentInvoice.setExtra(extra);

        paymentInvoiceRepository.save(paymentInvoice);
    }

    // lock
//    @Transactional
    public PayoutResponse createPayout(PayoutWithdrawalRequest request) {

        // todo pre transaction
        var paymentResponse = paymentHandler.payout(request);

        var walletHistory = walletService.createTransaction(request.getAmount(), request.getCurrency(), TypeTransaction.WITHDRAWAL);

        var responseElement = Arrays.stream(paymentResponse.getWithdrawals()).findFirst().get();

        var paymentInvoice = new PaymentInvoice();
        paymentInvoice.setAmount(request.getAmount());
        paymentInvoice.setWalletHistory(walletHistory);
        paymentInvoice.setExternalId(responseElement.getId());
        paymentInvoice.setStatus(responseElement.getStatus());

        var extra = new PaymentExtra();
        extra.setPayAddress(responseElement.getAddress());

        paymentInvoice.setExtra(extra);

        paymentInvoiceRepository.save(paymentInvoice);
        return paymentResponse;
    }
}
