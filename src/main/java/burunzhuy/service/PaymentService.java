package burunzhuy.service;

import burunzhuy.dto.pay.PaymentExtra;
import burunzhuy.entity.pay.PaymentInvoice;
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
    public void createInvoice(BigDecimal amount, String currency) {
        var walletHistory = walletService.createTransaction(amount, currency);

        // todo pre transaction
        var paymentResponse = paymentHandler.depositInnerBalance(amount, currency);

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
}
