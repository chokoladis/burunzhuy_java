package burunzhuy.service;

import burunzhuy.entity.Auction;
import burunzhuy.entity.AuctionHistory;
import burunzhuy.entity.Idea;
import burunzhuy.entity.User;
import burunzhuy.enums.auction.Status;
import burunzhuy.exception.auction.AccessException;
import burunzhuy.exception.auction.BidException;
import burunzhuy.exception.common.EntityNotFound;
import burunzhuy.repository.AuctionHistoryRepository;
import burunzhuy.repository.AuctionRepository;
import burunzhuy.resource.auction.HistoryResource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuctionHistoryService {

    private final AuctionRepository auctionRepository;
    private final AuctionHistoryRepository auctionHistoryRepository;
    private final ProfileService profileService;

    public Page<HistoryResource> getList(
            int page,
            int perPage
    ) {
        Pageable pageable = PageRequest.of(page, perPage, Sort.by("createdAt").descending());

        Page<AuctionHistory> pageData = auctionHistoryRepository.findAll(pageable);
        return pageData.map(HistoryResource::new);
    }

    public HistoryResource setBid(
            Long auctionId,
            BigDecimal bid
    ) {
        User currentUser = profileService.getCurrentUser();

        Auction auction = auctionRepository.findById(auctionId).orElse(null);
        if (auction == null) {
            throw new EntityNotFound("Аукцион не найден");
        }

        this.validate(auction, currentUser, bid);

        AuctionHistory newHistory = auctionHistoryRepository
                .findByAuctionIdAndBuyerId(auctionId, currentUser.getId())
                .orElseGet(AuctionHistory::new);

        if (newHistory.getBid() == null) {
            newHistory.setAuction(auction);
            newHistory.setBuyer(currentUser);
        }

        newHistory.setBid(bid);

        // todo списывание со счета, транзакция (блокировка строк по аукциону и истории)
        if (bid.compareTo(auction.getIdea().getPriceInstanceBuy()) != -1) {
            auction.setBuyer(currentUser);
            auction.setStatus(Status.SOLD);
            auctionRepository.save(auction);
        }

        return new HistoryResource(auctionHistoryRepository.save(newHistory));
    }

//    @Transactional
//    public void delete(
//        Long id
//    )
//    {
//        Idea idea = ideaRepository.findByIdAndOwnerId(id, profileService.getCurrentUserId());
//        if (idea != null) {
//            ideaRepository.delete(idea);
//        } else {
//            throw new EntityNotFound("Idea not be found");
//        }
//    }

    protected void validate(Auction auction, User currentUser, BigDecimal bid) {
        Idea idea = auction.getIdea();

        if (currentUser.getId().equals(idea.getOwner().getId())) {
            throw new AccessException("Вы не можете делать ставки в своем аукционе");
        }

        AuctionHistory currentTopAuctionBid = auctionHistoryRepository
                .findFirstByAuctionIdOrderByBidDesc(auction.getId())
                .orElse(null);

        if (bid.compareTo(currentTopAuctionBid.getBid()) != 1) {
            throw new BidException("Ставка должна быть больше текущей");
        }

        if (bid.compareTo(idea.getPriceMin()) == -1) {
            throw new BidException("Минимальная ставка - " + idea.getPriceMin());
        }

        if (!auction.getStatus().equals(Status.OPENED)) {
            throw new AccessException("Делать ставки можно только в открытом аукционе");
        }
    }
}
