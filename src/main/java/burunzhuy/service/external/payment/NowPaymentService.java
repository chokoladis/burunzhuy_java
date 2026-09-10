package burunzhuy.service.external.payment;

import burunzhuy.dto.external.pay.nowpayment.AuthRequest;
import burunzhuy.dto.external.pay.nowpayment.AuthResponse;
import burunzhuy.dto.external.pay.nowpayment.EstimatedPriceResponse;
import burunzhuy.dto.external.pay.nowpayment.PaymentRequest;
import burunzhuy.interfaces.PaymentInterface;
import burunzhuy.tool.Logger;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Locale;

@Service
@Primary
public class NowPaymentService implements PaymentInterface {

    @Value("${now_payments.email}")
    private String email;
    @Value("${now_payments.password}")
    private String password;
    @Value("${now_payments.api_key}")
    private String apiKey;
    @Value("${now_payments.api_key_test}")
    private String apiKeyTest;

    public static final String BASE_URL = "https://api.nowpayments.io/";
    public static final String BASE_TEST_URL = "https://api-sandbox.nowpayments.io/";

    public void depositInnerBalance(BigDecimal amount, String currency)
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
                .body(String.class);

        Logger.logToFile("nowpayment.txt", "result is " + result);
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
                .defaultHeader("x-api-key", this.apiKeyTest)
                .build();
    }

    //todo cache ?
    private EstimatedPriceResponse getPrice(BigDecimal amount, String currency){
        var url = String.format(Locale.US, "/v1/estimate?amount=%f&currency_from=%s&currency_to=%s", amount, "usd", currency);
        Logger.logToFile("nowpayment.txt", "url is " + url);

        return this.getClient()
            .get()
            .uri(url)
//            .header("x-api-key", this.apiKey)
            .retrieve()
            .body(EstimatedPriceResponse.class);
    }

}
