package burunzhuy.exception.pay;

import java.math.BigDecimal;

public class PayException extends RuntimeException {
    public PayException(String message) {
        super(message);
    }

    public PayException(String message, BigDecimal available) {
        super(String.format(message, available));
    }
}
