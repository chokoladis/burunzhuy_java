package burunzhuy.dto.auction;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
final public class SetBidRequest {

    @NotNull
    private Long auctionId;

    private BigDecimal bid;
}
