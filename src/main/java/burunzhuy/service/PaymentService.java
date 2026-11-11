package burunzhuy.service;

import burunzhuy.dto.pay.PaymentExtra;
import burunzhuy.dto.pay.WithdrawRequest;
import burunzhuy.entity.pay.PaymentInvoice;
import burunzhuy.entity.pay.Wallet;
import burunzhuy.enums.pay.TypeTransaction;
import burunzhuy.exception.pay.PayException;
import burunzhuy.interfaces.PaymentInterface;
import burunzhuy.repository.pay.PaymentInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentInterface paymentHandler;
    private final WalletService walletService;
    private final PaymentInvoiceRepository paymentInvoiceRepository;

//    todo transaction
//    @Transactional
    public void deposit(BigDecimal amount, String currency) {
        Wallet wallet = walletService.firstOrCreate(currency);
        var walletHistory = walletService.addTransaction(wallet, amount, TypeTransaction.DEPOSIT);

        // todo pre transaction
        var paymentResponse = paymentHandler.deposit(amount, currency);

        var paymentInvoice = new PaymentInvoice();
        paymentInvoice.setAmount(amount);
        paymentInvoice.setWalletHistory(walletHistory);
        paymentInvoice.setExternalId(paymentResponse.getPayment_id());
        paymentInvoice.setStatus(paymentResponse.getPayment_status());
        paymentInvoice.setExpirationAt(paymentResponse.getExpiration_estimate_date());

        var extra = new PaymentExtra();
        extra.setPay_amount(paymentResponse.getPay_amount());
        extra.setPay_address(paymentResponse.getPay_address());

        paymentInvoice.setExtra(extra);

        paymentInvoiceRepository.save(paymentInvoice);
    }

    public void withdraw(
        WithdrawRequest request
    )
    {
        Wallet wallet = walletService.getByCurrencyAndOwner(request.getCurrency())
            .orElseThrow();
        ;
        if (request.getAmount().compareTo(wallet.getBalance()) == 1) {
            throw new PayException(String.format("Превышен лимит средств на счете %s, доступно %f",  request.getCurrency(), wallet.getBalance()));
        }

        //transaction
        walletService.addTransaction(wallet, request.getAmount(), TypeTransaction.WITHDRAW);

        //todo send message to rabbitmq
        //todo handle a output
        var result = paymentHandler.withdraw(request);
    }
}
