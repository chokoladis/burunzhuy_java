package burunzhuy.enums.pay;

public enum TypeTransaction {
    DEPOSIT, WITHDRAWAL,
    BUY, SELL;
//    CHARGE; ?

    public Boolean isMoneyOut() {
        return this.equals(WITHDRAWAL) || this.equals(BUY);
    }
}
