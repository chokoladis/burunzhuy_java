package burunzhuy.enums.pay;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Status {
    // no handle - confirmed / confirming
    WAITING, SENDING,
    PARTIALLY_PAYED, PAYED,
    FINISHED, CANCELED, REFUNDED,
    FAILED, EXPIRED
}
