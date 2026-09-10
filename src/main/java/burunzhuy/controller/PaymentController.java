package burunzhuy.controller;

import burunzhuy.dto.http.ApiResponse;
import burunzhuy.dto.pay.DepositRequest;
import burunzhuy.exception.pay.PayException;
import burunzhuy.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/payment/")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Currently following fiat currencies are available: USD, EUR, KZT, CHF, RUB, TRY, etc.
     * @param depositRequest
     * @return
     */
    // "id": 52,
    //            "code": "USDTTRC20",
    //            "name": "Tether USD (Tron)",
    //            "enable": true,
    //            "wallet_regex": "^T[1-9A-HJ-NP-Za-km-z]{33}$",
    //            "priority": 10,
    //            "extra_id_exists": false,
    //            "extra_id_regex": null,
    //            "logo_url": "/images/coins/usdttrc20.svg",
    //            "track": true,
    //            "cg_id": "tether",
    //            "is_maxlimit": false,
    //            "network": "trx",
    //            "smart_contract": "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
    //            "network_precision": "6",
    //            "explorer_link_hash": "https://tronscan.org/#/transaction/:hash",
    //            "precision": 6,
    //            "ticker": "usdt",
    //            "is_defi": false,
    //            "is_popular": false,
    //            "is_stable": true,
    //            "available_for_to_conversion": true,
    //            "trust_wallet_id": null,
    //            "created_at": "2024-08-28T16:06:33.998Z",
    //            "updated_at": "2025-11-28T11:25:03.276Z",
    //            "available_for_payment": true,
    //            "available_for_payout": true,
    //            "extra_id_optional": false
    @PostMapping("deposit")
    public ResponseEntity<?> deposit(
        @Valid @RequestBody DepositRequest depositRequest
    ) {
        try {
            BigDecimal amount = new BigDecimal(depositRequest.amount);
            if (amount.compareTo(new BigDecimal(2)) == -1) {
                //todo currency
                throw new PayException("Минимальная сумма депозита 2$");
            }

            paymentService.createInvoice(amount, depositRequest.currency.toLowerCase());

            return ResponseEntity.status(201).build();
        } catch (PayException e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
