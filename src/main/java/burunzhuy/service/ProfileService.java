package burunzhuy.service;

import burunzhuy.entity.User;
import burunzhuy.exception.auth.UserException;
import burunzhuy.repository.UserRepository;
import burunzhuy.resource.user.UserResource;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;

    public User getCurrentUser()
    {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("Пользователь не был найден");
        }

        return user;
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
}
