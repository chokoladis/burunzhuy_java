package burunzhuy.service;

import burunzhuy.dto.auth.RegisterRequest;
import burunzhuy.entity.User;
import burunzhuy.enums.user.Role;
import burunzhuy.exception.auth.RegisterException;
import burunzhuy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())){
            throw new RegisterException("Пользователь с данными email уже существует");
        }

        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        newUser.setName(request.getName());
        newUser.setSecondName(request.getSecondName());
        newUser.setLastName(request.getLastName());
        newUser.setRole(Set.of(Role.BUYER, Role.SELLER));

        String phoneNumber = request.getPhone().replaceAll("[\\D]+", "");

        newUser.setPhone(
            Long.parseLong(phoneNumber)
        );
        return userRepository.save(newUser);
    }
}
