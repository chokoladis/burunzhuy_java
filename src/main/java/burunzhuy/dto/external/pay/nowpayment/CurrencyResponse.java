package burunzhuy.dto.external.pay.nowpayment;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CurrencyResponse implements Serializable {
    private CurrencyBodyResponse[] currencies;
}
