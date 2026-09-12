package burunzhuy.controller;

import burunzhuy.dto.http.ApiResponse;
import burunzhuy.dto.pay.DepositRequest;
import burunzhuy.exception.pay.PayException;
import burunzhuy.helper.StringHelper;
import burunzhuy.service.PaymentService;
import burunzhuy.service.external.payment.NowPaymentService;
import burunzhuy.tool.Logger;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment/")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final NowPaymentService nowPaymentService;

    /**
     * Currently following fiat currencies are available: USD, EUR, KZT, CHF, RUB, TRY, etc.
     * @param depositRequest
     * @return
     */
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

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (PayException e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("webhook/nowpayment")
    public ResponseEntity<?> nowPaymentWebhook(
            @RequestHeader(value = "X-Nowpayments-Sig", required = false) String receivedHmac,
            @RequestBody String rawBody
    ){
        Logger.logToFile("paywebhook.txt", "raw - " + rawBody);

        try {
            if (receivedHmac == null || receivedHmac.isBlank()) {
                throw new IllegalArgumentException("No HMAC signature sent.");
            }

            var mapper = StringHelper.getJsonReader();
            JsonNode nodeResponse = mapper.readTree(rawBody);

            if (!nowPaymentService.verifySignature(receivedHmac, nodeResponse)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Invalid request"));
            }

            // todo dto ?
            nowPaymentService.handleWebhook(nodeResponse);

            Logger.logToFile("paywebhook.txt", "success");

            return ResponseEntity.ok().build();
        } catch (Throwable e) {
            e.printStackTrace();

            Logger.logToFile("paywebhook.txt", "err - " + e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Invalid request"));
        }

    }
}
