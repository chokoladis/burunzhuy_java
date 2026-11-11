package burunzhuy.controller;

import burunzhuy.dto.http.ApiResponse;
import burunzhuy.interfaces.PaymentInterface;
import burunzhuy.service.PaymentSystemService;
import burunzhuy.tool.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/api/v1/payment-system/")
@RequiredArgsConstructor
public class PaymentSystemController {

    private final PaymentSystemService paymentSystemService;
    private final List<PaymentInterface> paymentServices;

    @GetMapping("")
    public ResponseEntity<?> getPaymentList() {
        return ResponseEntity.ok(ApiResponse.ok(paymentSystemService.getList()));
    }

    @GetMapping("{code}/currency/")
    public ResponseEntity<?> getCurrencies(@PathVariable String code) {
        try {

            for (PaymentInterface instance : paymentServices) {
                if (instance.getCode().contentEquals(code)){

                    var result = instance.getCurrencies();
                    if (result.isPresent())
                        return ResponseEntity.ok().body(result.get());
                }
            }

            //todo get hanler class by code ?
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            Logger.logToFile("errors.txt", e.getMessage() + " : " + e.getClass());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
