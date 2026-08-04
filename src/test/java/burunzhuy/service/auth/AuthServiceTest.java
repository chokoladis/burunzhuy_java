package burunzhuy.service.auth;

import burunzhuy.dto.auth.LoginRequest;
import burunzhuy.dto.auth.RegisterRequest;
import burunzhuy.dto.jwt.JwtResponse;
import burunzhuy.entity.user.User;
import burunzhuy.exception.auth.RegisterException;
import burunzhuy.helper.JwtHelper;
import burunzhuy.helper.UserHelper;
import burunzhuy.repository.RoleRepository;
import burunzhuy.repository.UserRepository;
import burunzhuy.repository.security.RefreshTokenRepository;
import burunzhuy.service.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import javax.security.auth.login.FailedLoginException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private UserHelper userHelper;
    @Mock
    private JwtHelper jwtHelper;

    private AuthService authService;

    private final String defaultEmail = "test@mail.ru";
    private final String defaultPassword = "123MoooasWP3";
    private final Long defaultPhone = 79123456789L;
    private final String defaultName = "testname";


    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, roleRepository, passwordEncoder, jwtService, refreshTokenRepository, userHelper, jwtHelper);
    }

    @Test
    void register_returnNull() {
        var request = new RegisterRequest();
        var response = authService.register(request);
        assertNull(response);
    }

    @Test
    void register_success() {
        var request = new RegisterRequest();
        request.setEmail(this.defaultEmail);
        request.setName(this.defaultName);
        request.setPassword(this.defaultPassword);
        request.setPhone("+"+this.defaultPhone);

        when(userRepository.existsByEmail(this.defaultEmail)).thenReturn(false);
        when(passwordEncoder.encode(this.defaultPassword)).thenReturn("hashed");
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        assertInstanceOf(User.class, authService.register(request));
    }

    @Test
    void register_errorExistsByEmail() {
        var request = new RegisterRequest();
        request.setEmail(this.defaultEmail);
        request.setName(this.defaultName);
        request.setPassword(this.defaultPassword);
        request.setPhone("+" + this.defaultPhone);

        when(userRepository.existsByEmail(this.defaultEmail)).thenReturn(true);

        assertThrows(RegisterException.class, () -> {
            authService.register(request);
        });
    }

    @Test
    void login_success() throws FailedLoginException {
        LoginRequest request = new LoginRequest();
        request.setEmail(this.defaultEmail);
        request.setPassword(this.defaultPassword);

        HttpServletRequest httpRequest = mock(HttpServletRequest.class);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(getDefaultUser());
        when(passwordEncoder.matches(request.getPassword(), this.getDefaultUser().getPassword())).thenReturn(true);
        when(jwtService.getNewTokens(this.defaultEmail)).thenReturn(new JwtResponse("accessToken", "refreshToken"));

        JwtResponse response = authService.login(request, httpRequest);

        assertNotNull(response);
    }

    @Test
    void login_errorUserNotFound() throws FailedLoginException {
        LoginRequest request = new LoginRequest();
        request.setPassword(this.defaultPassword);

        HttpServletRequest httpRequest = mock(HttpServletRequest.class);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(null);

        assertThrows(FailedLoginException.class, () -> {
            authService.login(request, httpRequest);
        });
    }

    final User getDefaultUser() {
        User user = new User();
        user.setEmail(this.defaultEmail);
        user.setPassword(this.defaultPassword); //passwordEncoder.encode(this.defaultPassword)
        user.setPhone(this.defaultPhone);
        user.setName(this.defaultName);
        return user;
    }
}