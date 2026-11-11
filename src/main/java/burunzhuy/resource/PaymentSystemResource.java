package burunzhuy.resource;

import burunzhuy.entity.pay.PaymentSystem;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PaymentSystemResource {
    private String name;
    private String code;
    private String description;
    private String extra;

    public PaymentSystemResource(PaymentSystem entity)
    {
        name = entity.getName();
        code = entity.getCode();
        description = entity.getDescription();
        extra = entity.getExtra();
    }
}
