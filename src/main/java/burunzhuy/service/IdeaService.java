package burunzhuy.service;

import burunzhuy.entity.Idea;
import burunzhuy.entity.User;
import burunzhuy.exception.auth.UserException;
import burunzhuy.repository.IdeaRepository;
import burunzhuy.repository.UserRepository;
import burunzhuy.resource.idea.FullResource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdeaService {

    private final IdeaRepository ideaRepository;
    private final UserRepository userRepository;

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
}
