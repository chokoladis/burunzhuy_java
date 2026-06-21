package burunzhuy.service;

import burunzhuy.dto.idea.CreateRequest;
import burunzhuy.dto.idea.UpdateRequest;
import burunzhuy.entity.File;
import burunzhuy.entity.Idea;
import burunzhuy.exception.common.EntityNotFound;
import burunzhuy.repository.IdeaRepository;
import burunzhuy.resource.idea.FullResource;
import burunzhuy.tool.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class IdeaService {

    private final IdeaRepository ideaRepository;
    private final ProfileService profileService;
    private final FileService fileService;

    public Page<FullResource> getForCurrentUser(
        int page,
        int perPage
    )
    {
        Pageable pageable = PageRequest.of(page, perPage, Sort.by("createdAt").descending());

        Page<Idea> pageIdea = ideaRepository.findByOwnerId(
                profileService.getCurrentUserId(), pageable
        );
        return pageIdea.map(FullResource::new);
    }

    public FullResource getById(
        Long id
    )
    {
        Idea idea = ideaRepository.findByIdAndOwnerId(id, profileService.getCurrentUserId());
        return new FullResource(idea);
    }


    @Transactional
    public FullResource create(
        CreateRequest request,
        MultipartFile preview,
        MultipartFile[] attaches
    )
    {
        Idea newIdea = new Idea();

        newIdea.setOwner(profileService.getCurrentUser());
        newIdea.setTitle(request.getTitle());
        newIdea.setShortDescription(request.getShortDescription());
        newIdea.setFullDescription(request.getFullDescription());
        newIdea.setPriceMin(request.getPriceMin());
        newIdea.setPriceInstanceBuy(request.getPriceInstanceBuy());

        // todo remove files
        // todo validate as img
        try {
            newIdea.setPreview(fileService.save(preview, "ideas"));
        } catch (Throwable e) {
            e.printStackTrace();
            Logger.logToFile("idea.txt", e.getMessage());
        }

        if (attaches != null && attaches.length > 0){
            Set<File> files = new HashSet<>();
            for (var file: attaches) {
                files.add(fileService.save(file, "ideas"));
            }
            newIdea.setAttaches(files);
        }

        return new FullResource(ideaRepository.save(newIdea));
    }

    @Transactional
    public FullResource update(
            Long id,
            UpdateRequest request,
            MultipartFile preview,
            MultipartFile[] attaches
    )
    {
        Idea idea = ideaRepository.findByIdAndOwnerId(id, profileService.getCurrentUserId());

        if (request.getTitle() != null) idea.setTitle(request.getTitle());
        if (request.getShortDescription() != null) idea.setShortDescription(request.getShortDescription());
        if (request.getFullDescription() != null) idea.setFullDescription(request.getFullDescription());
        if (request.getPriceMin() != null) idea.setPriceMin(request.getPriceMin());
        if (request.getPriceInstanceBuy() != null) idea.setPriceInstanceBuy(request.getPriceInstanceBuy());

        if (preview != null) idea.setPreview(fileService.save(preview, "ideas"));

        if (attaches != null && attaches.length > 0){
            Set<File> files = new HashSet<>();
            for (var file: attaches) {
                files.add(fileService.save(file, "ideas"));
            }
            idea.setAttaches(files);
        }

        return new FullResource(ideaRepository.save(idea));
    }

    @Transactional
    public void delete(
        Long id
    )
    {
        Idea idea = ideaRepository.findByIdAndOwnerId(id, profileService.getCurrentUserId());
        if (idea != null) {
            ideaRepository.delete(idea);
        } else {
            throw new EntityNotFound("Idea not be found");
        }
    }

}
