package burunzhuy.enums.pay;

public enum Status {
    SENDING, PARTIALLY_PAYED,
    // confirmed or payed ?
    PAYED, CANCELED, REFUNDED,
    WAITING, FAILED, EXPIRED
}
