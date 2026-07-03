package burunzhuy.service.user;

import burunzhuy.dto.jwt.JwtResponse;
import burunzhuy.dto.auth.LoginRequest;
import burunzhuy.dto.auth.RegisterRequest;
import burunzhuy.entity.user.Role;
import burunzhuy.entity.user.User;
import burunzhuy.enums.user.RoleEnum;
import burunzhuy.exception.auth.RegisterException;
import burunzhuy.repository.RoleRepository;
import burunzhuy.repository.UserRepository;
import burunzhuy.service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.FailedLoginException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegisterException("Пользователь с данными email уже существует");
        }

        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        newUser.setName(request.getName());
        newUser.setSecondName(request.getSecondName());
        newUser.setLastName(request.getLastName());
        newUser.setRoles(this.getDefaultRoles());

        if (request.getPhone() != null) {
            String phoneNumber = request.getPhone().replaceAll("[\\D]+", "");
            newUser.setPhone(
                    Long.parseLong(phoneNumber)
            );
        }

        return userRepository.save(newUser);
    }

    private Set<Role> getDefaultRoles() {

        Set<RoleEnum> setRoles = Set.of(RoleEnum.BUYER, RoleEnum.SELLER);

        return roleRepository.findByNameIn(setRoles);
    }

    public JwtResponse login(LoginRequest request) throws FailedLoginException {
        User user = userRepository.findByEmail(request.getEmail());

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new FailedLoginException("Не верный email или пароль");
        }

        return jwtService.getNewTokens(user.getEmail());
    }
}
