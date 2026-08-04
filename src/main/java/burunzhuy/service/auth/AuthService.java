package burunzhuy.service.auth;

import burunzhuy.dto.jwt.JwtResponse;
import burunzhuy.dto.auth.LoginRequest;
import burunzhuy.dto.auth.RegisterRequest;
import burunzhuy.entity.security.RefreshToken;
import burunzhuy.entity.user.Role;
import burunzhuy.entity.user.User;
import burunzhuy.enums.user.RoleEnum;
import burunzhuy.exception.auth.RegisterException;
import burunzhuy.helper.JwtHelper;
import burunzhuy.helper.SecureHelper;
import burunzhuy.helper.UserHelper;
import burunzhuy.repository.RoleRepository;
import burunzhuy.repository.UserRepository;
import burunzhuy.repository.security.RefreshTokenRepository;
import burunzhuy.service.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserHelper userHelper;
    private final JwtHelper jwtHelper;

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

    public JwtResponse login(LoginRequest request, HttpServletRequest httpRequest)
            throws FailedLoginException
    {
        User user = userRepository.findByEmail(request.getEmail());

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new FailedLoginException("Не верный email или пароль");
        }

        JwtResponse jwtResponse = jwtService.getNewTokens(user.getEmail());

        // проверку перед добавлением, мб для апи и отпечатка есть дейст. токен, отдать его
        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setToken(SecureHelper.getHashedValue(jwtResponse.getRefreshToken()));
        newRefreshToken.setUser(user);

        newRefreshToken.setDeviceFingerprint(UserHelper.getDeviceFingerprint(httpRequest));
        newRefreshToken.setIpAddress(userHelper.getIpAddress(httpRequest));
        newRefreshToken.setExpiredAt(jwtHelper.getExpiredAtRefreshToken());

        refreshTokenRepository.save(newRefreshToken);

        return jwtResponse;
    }
}
