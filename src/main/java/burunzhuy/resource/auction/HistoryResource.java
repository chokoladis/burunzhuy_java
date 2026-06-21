package burunzhuy.resource.auction;

import burunzhuy.entity.AuctionHistory;
import burunzhuy.resource.user.UserResource;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({"id", "buyer", "bid", "createdAt", "updatedAt"})
final public class HistoryResource {

    private final Long id;
    private final UserResource buyer;
    private final BigDecimal bid;

    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public HistoryResource(AuctionHistory history) {
        this.id = history.getId();
        this.buyer = new UserResource(history.getBuyer());
        this.bid = history.getBid();
        this.createdAt = history.getCreatedAt();
        this.updatedAt = history.getUpdatedAt();
    }
}
