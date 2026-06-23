package burunzhuy.repository;

import burunzhuy.entity.Auction;
import burunzhuy.entity.AuctionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AuctionHistoryRepository extends JpaRepository<AuctionHistory, Long> {
    Optional<AuctionHistory> findByAuctionIdAndBuyerId(Long auctionId, Long buyerId);
    Optional<AuctionHistory> findFirstByAuctionIdOrderByBidDesc(Long auctionId);
}
