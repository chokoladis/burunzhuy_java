package burunzhuy.repository;

import burunzhuy.entity.AuctionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuctionHistoryRepository extends JpaRepository<AuctionHistory, Long> {
    Optional<AuctionHistory> findByAuctionIdAndBuyerId(Long auctionId, Long buyerId);

    Optional<AuctionHistory> findFirstByAuctionIdOrderByBidDesc(Long auctionId);
}
