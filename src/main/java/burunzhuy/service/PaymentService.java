package burunzhuy.service;

import burunzhuy.interfaces.PaymentInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentInterface paymentHandler;
    private final WalletService walletService;

//    todo transaction
//    @Transactional
    public void createInvoice(BigDecimal amount, String currency) {
        var walletHistory = walletService.createTransaction(amount, currency);

        // todo pre transaction
        paymentHandler.depositInnerBalance(amount, currency);

//        var paymentInvoice = new PaymentInvoice();
//        paymentInvoice.setWalletHistory(walletHistory);
//        paymentInvoice.setUrl("");
    }
}
