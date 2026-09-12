package burunzhuy.enums.pay;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Status {
    SENDING, PARTIALLY_PAYED,
    // confirmed or payed ?
    PAYED, CANCELED, REFUNDED,
    WAITING, FINISHED, FAILED, EXPIRED
}
