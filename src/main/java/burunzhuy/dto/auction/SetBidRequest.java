package burunzhuy.dto.auction;

import burunzhuy.entity.Auction;
import burunzhuy.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
final public class SetBidRequest {

    @NotNull
    private Long auctionId;

    private BigDecimal bid;
}
