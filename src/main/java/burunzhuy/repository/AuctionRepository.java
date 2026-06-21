package burunzhuy.repository;

import burunzhuy.entity.Auction;
import burunzhuy.entity.Idea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    Page<Auction> findByIdeaOwnerId(Long ownerId, Pageable pageable);
}
