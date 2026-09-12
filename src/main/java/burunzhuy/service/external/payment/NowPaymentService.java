package burunzhuy.service.external.payment;

import burunzhuy.dto.external.pay.nowpayment.*;
import burunzhuy.entity.pay.PaymentInvoice;
import burunzhuy.enums.pay.Status;
import burunzhuy.exception.common.EntityNotFound;
import burunzhuy.helper.StringHelper;
import burunzhuy.interfaces.PaymentInterface;
import burunzhuy.repository.pay.PaymentInvoiceRepository;
import burunzhuy.tool.Logger;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.Optional;

@Service
@Primary
@RequiredArgsConstructor
public class NowPaymentService implements PaymentInterface {

    @Value("${now_payments.api_key}")
    private String apiKey;
    @Value("${now_payments.api_key_test}")
    private String apiKeyTest;
    @Value("${now_payments.ipn_secret_test}")
    private String ipnSecretTest;

    public static final String BASE_URL = "https://api.nowpayments.io/";
    public static final String BASE_TEST_URL = "https://api-sandbox.nowpayments.io/";

    private final PaymentInvoiceRepository paymentInvoiceRepository;

    public PaymentResponse depositInnerBalance(BigDecimal amount, String currency)
    {
        var priceResponse = getPrice(amount, currency);

        var request = new PaymentRequest(
            amount,
            "usd",
            priceResponse.estimated_amount(),
            currency
        );

        Logger.logToFile("nowpayment.txt", request.toString());

        var result = this.getClient()
                .post()
                .uri("/v1/payment")
                .body(request)
                .retrieve()
                .body(PaymentResponse.class);

        Logger.logToFile("nowpayment.txt", "result is " + result);

        return result;
    }

    public void withdrawInnerBalance() {
    }

    public void createInvoice()
    {
//    curl --location 'https://api.nowpayments.io/v1/invoice'\
//            --header 'x-api-key: ***'
//            --header 'Content-Type: application/json'\
//            --data '{
//            "price_amount": "10",
//            "price_currency": "USD",
//            "pay_currency": "",
//            "ipn_callback_url": "https://192.168.0.1/payment/notifications",
//            "order_description": ""
//}'
    }

    private RestClient getClient() {
        return getClient(true);
    }

    private RestClient getClient(boolean isTest) {
        return RestClient.builder()
                .baseUrl(isTest ? BASE_TEST_URL : BASE_URL)
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("x-api-key", isTest ? this.apiKeyTest : this.apiKey)
                .build();
    }

    //todo cache ?
    private EstimatedPriceResponse getPrice(BigDecimal amount, String currency){
        var url = String.format(Locale.US, "/v1/estimate?amount=%f&currency_from=%s&currency_to=%s", amount, "usd", currency);
        Logger.logToFile("nowpayment.txt", "url is " + url);

        return this.getClient()
            .get()
            .uri(url)
            .retrieve()
            .body(EstimatedPriceResponse.class);
    }

    public boolean verifySignature(
            String receivedHmac,
            JsonNode nodeResponse
    ) {
        try {
            var mapper = StringHelper.getJsonReader();
            JsonNode sorted = StringHelper.sortNode(nodeResponse, mapper);
            String sortedNode = mapper.writeValueAsString(sorted);

            String calculatedHmac = calculateHmac(sortedNode, ipnSecretTest.trim());

            return MessageDigest.isEqual(
                    calculatedHmac.getBytes(StandardCharsets.UTF_8),
                    receivedHmac.getBytes(StandardCharsets.UTF_8)
            );
        } catch (Exception e) {
            Logger.logToFile("nowpayment.txt", "verify sign err - " + e.getMessage());
            return false;
        }
    }

    public static String calculateHmac(String data, String secret) throws Exception {
        String algo = "HmacSHA512";

        Mac sha512Hmac = Mac.getInstance(algo);
        SecretKeySpec keySpec = new SecretKeySpec(
            secret.getBytes(StandardCharsets.UTF_8),
            algo
        );
        sha512Hmac.init(keySpec);

        return StringHelper.bytesToHex(
            sha512Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8))
        );
    }

    public void handleWebhook(
        JsonNode nodeResponse
    ) {
    //        nodeResponse.get("price_amount");
    //        nodeResponse.get("price_currency").asText();
    //        nodeResponse.get("pay_amount");
    //        nodeResponse.get("pay_currency").asText();
    //        nodeResponse.get("outcome_amount").asDouble();
    //        nodeResponse.get("outcome_currency").asText();

        Optional<PaymentInvoice> invoice = paymentInvoiceRepository.findByExternalId(String.valueOf(nodeResponse.get("payment_id").asLong()));
        if (invoice.isEmpty()) {
            Logger.logToFile("nowpayment.txt", "unknown invoice - " + nodeResponse.get("payment_id"));
            throw new EntityNotFound("invoice not found");
        }

        var invoiceObj = invoice.get();

        try {
            invoiceObj.setStatus(
                Status.valueOf(nodeResponse.get("payment_status").asText().toUpperCase())
            );
        } catch (IllegalArgumentException e) {
            Logger.logToFile("nowpayment.txt", "unknown status - " + nodeResponse.get("payment_status").asText());
            throw e;
        }

        invoiceObj.setActuallyPaid(new BigDecimal(nodeResponse.get("actually_paid").asText()));
        paymentInvoiceRepository.save(invoiceObj);
    }

}
