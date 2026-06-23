package burunzhuy.resource.auction;

import burunzhuy.entity.Auction;
import burunzhuy.resource.idea.ShortResource;
import burunzhuy.resource.user.UserResource;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@JsonPropertyOrder({"id", "idea", "buyer", "status", "createdAt", "finishedAt", "history"})
final public class AuctionResource {

    private final Long id;
    private final ShortResource idea;
    private final UserResource buyer;
    private final String status;
    private final Set<HistoryResource> history;

    private final LocalDateTime createdAt;
    private final LocalDateTime finishedAt;

    public AuctionResource(Auction auction) {
        this.id = auction.getId();
        this.idea = new ShortResource(auction.getIdea());
        this.buyer = auction.getBuyer() != null ? new UserResource(auction.getBuyer()) : null;
        this.status = auction.getStatus().name();
//        todo paginate
        this.history = auction.getHistory()
                .stream()
                .map(history -> new HistoryResource(history))
                .collect(Collectors.toSet());

        this.createdAt = auction.getCreatedAt();
        this.finishedAt = auction.getFinishedAt();
    }
}
