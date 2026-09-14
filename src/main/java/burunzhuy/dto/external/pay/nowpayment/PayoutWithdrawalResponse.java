package burunzhuy.dto.external.pay.nowpayment;

import burunzhuy.enums.pay.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PayoutWithdrawalResponse {

        private String id;
        private String address;
        private String currency;
        private String amount;
        @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
        private Status status;

        private LocalDateTime created_at;
        private LocalDateTime requested_at;
        private LocalDateTime updated_at;

//        "hash": null,
//        "error": null,
//        "unique_external_id": null,
}
