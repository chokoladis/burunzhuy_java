package burunzhuy.dto.external.pay.nowpayment;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
/**
 * @apiNote in sandbox: network, dateTime - can be null
 */
public class CurrencyBodyResponse implements Serializable {

    private String code; // использовать как currency для /payment/deposit
    private String name;
    //    private String logo_url; // todo путь откуда ? "/images/coins/usdttrc20.svg",
    private String network;

    private String smart_contract;
    private Integer precision;
    private String ticker;
    private Boolean is_stable;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    // "id": 52,
    // "enable": true
    // "track": true,
    // "cg_id": "tether",
    // "explorer_link_hash": "https://tronscan.org/#/transaction/:hash",
    // "available_for_payment": true,
    // "available_for_payout": true,
}
