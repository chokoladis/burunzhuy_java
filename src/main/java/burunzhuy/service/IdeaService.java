package burunzhuy.service;

import burunzhuy.dto.idea.CreateRequest;
import burunzhuy.entity.File;
import burunzhuy.entity.Idea;
import burunzhuy.entity.User;
import burunzhuy.exception.auth.UserException;
import burunzhuy.repository.IdeaRepository;
import burunzhuy.repository.UserRepository;
import burunzhuy.resource.idea.FullResource;
import burunzhuy.tool.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class IdeaService {

    private final IdeaRepository ideaRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    public Page<FullResource> getForCurrentUser(
        int page,
        int perPage
    )
    {
        // todo закинуть куда то в один метод получение id юзера из контекста (middleware)

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        IO.println("email is "+email);

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("user by email not found");
        }


        Pageable pageable = PageRequest.of(page, perPage, Sort.by("createdAt").descending());

        Page<Idea> pageIdea = ideaRepository.findByOwnerId(user.getId(), pageable);
        return pageIdea.map(FullResource::new);
    }

    @Transactional
    public FullResource create(
        CreateRequest request,
        MultipartFile preview,
        MultipartFile[] attaches
    )
    {
        String email = SecurityContextHolder
                .getContext().getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("user by email not found");
        }

        Idea newIdea = new Idea();

        newIdea.setOwner(user);
        newIdea.setTitle(request.getTitle());
        newIdea.setShortDescription(request.getShortDescription());
        newIdea.setFullDescription(request.getFullDescription());
        newIdea.setPriceMin(request.getPriceMin());
        newIdea.setPriceInstanceBuy(request.getPriceInstanceBuy());

        //todo remove files
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
}
