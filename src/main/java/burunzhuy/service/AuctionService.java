package burunzhuy.service;

import burunzhuy.dto.auction.CreateRequest;
import burunzhuy.entity.Auction;
import burunzhuy.entity.Idea;
import burunzhuy.enums.auction.Status;
import burunzhuy.exception.common.EntityNotFound;
import burunzhuy.repository.AuctionRepository;
import burunzhuy.repository.IdeaRepository;
import burunzhuy.resource.auction.AuctionResource;
import burunzhuy.tool.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final IdeaRepository ideaRepository;
    private final ProfileService profileService;

    public Page<AuctionResource> getList(
        int page,
        int perPage
    ) {
        Pageable pageable = PageRequest.of(page, perPage, Sort.by("createdAt").descending());

        Page<Auction> pageData = auctionRepository.findAll(pageable);
        return pageData.map(AuctionResource::new);
    }

    public Page<AuctionResource> getForCurrentUser(
        int page,
        int perPage
    )
    {
        Pageable pageable = PageRequest.of(page, perPage, Sort.by("createdAt").descending());

        Page<Auction> pageIdea = auctionRepository.findByIdeaOwnerId(
                profileService.getCurrentUserId(), pageable
        );
        return pageIdea.map(AuctionResource::new);
    }

    public AuctionResource getById(
        Long id
    )
    {
        Auction auction = auctionRepository.findById(id).orElse(null);
        if (auction != null){
            return new AuctionResource(auction);
        } else {
            return null;
        }
    }


    @Transactional
    public AuctionResource create(
         CreateRequest request
    )
    {
        Idea idea = ideaRepository.findById(request.getIdeaId()).orElse(null);

        if (idea == null) {
            throw new EntityNotFound("Идея не была найдена");
        }

        Auction newAuction = new Auction();

        newAuction.setIdea(idea);
        newAuction.setStatus(Status.OPENED);
        newAuction.setFinishedAt(request.getFinishedAt() != null ? request.getFinishedAt() : LocalDateTime.now());

        return new AuctionResource(auctionRepository.save(newAuction));
    }

//    @Transactional
//    public AuctionResource update(
//            Long id,
//            UpdateRequest request,
//            MultipartFile preview,
//            MultipartFile[] attaches
//    )
//    {
//        Idea idea = ideaRepository.findByIdAndOwnerId(id, profileService.getCurrentUserId());
//
//        if (request.getTitle() != null) idea.setTitle(request.getTitle());
//        if (request.getShortDescription() != null) idea.setShortDescription(request.getShortDescription());
//        if (request.getFullDescription() != null) idea.setFullDescription(request.getFullDescription());
//        if (request.getPriceMin() != null) idea.setPriceMin(request.getPriceMin());
//        if (request.getPriceInstanceBuy() != null) idea.setPriceInstanceBuy(request.getPriceInstanceBuy());
//
//        if (preview != null) idea.setPreview(fileService.save(preview, "ideas"));
//
//        if (attaches != null && attaches.length > 0){
//            Set<File> files = new HashSet<>();
//            for (var file: attaches) {
//                files.add(fileService.save(file, "ideas"));
//            }
//            idea.setAttaches(files);
//        }
//
//        return new FullResource(ideaRepository.save(idea));
//    }
//
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

}
