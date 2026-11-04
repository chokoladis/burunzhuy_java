package burunzhuy.dto.external.pay.nowpayment;

import burunzhuy.enums.pay.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class WithdrawItemResponse {

    private String address;
    private String currency;
    private String amount;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
    private Status status;
    private String hash;
//             "error": null,
//             "payout_description": null,
//             "unique_external_id": null,
//             "created_at": "2020-11-12T17:06:12.791Z",
//             "requested_at": null,
//             "updated_at": null
}
