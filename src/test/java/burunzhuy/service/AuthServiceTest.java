package burunzhuy.service;

import burunzhuy.dto.auth.RegisterRequest;
import burunzhuy.entity.user.User;
import burunzhuy.exception.auth.RegisterException;
import burunzhuy.repository.RoleRepository;
import burunzhuy.repository.UserRepository;
import burunzhuy.service.security.JwtService;
import burunzhuy.service.auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

    private AuthService authService;


    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, roleRepository, passwordEncoder, jwtService);
    }

    @Test
    void registerReturnNull() {
        var request = new RegisterRequest();
        var response = authService.register(request);
        assertNull(response);
    }

    @Test
    void registerSuccessRegister() {
        var request = new RegisterRequest();
        request.setEmail("test@mail.ru");
        request.setName("test");
        request.setPassword("123MoooasWP3");
        request.setPhone("+79229292999");

        IO.println("test");
        when(userRepository.existsByEmail("test@mail.ru")).thenReturn(false);
        when(passwordEncoder.encode("123MoooasWP3")).thenReturn("hashed");
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var response = authService.register(request);
//        IO.println(response);
        assertInstanceOf(User.class, response);
    }

    @Test
    void registerReturnErrorExistsByEmail() {
        var request = new RegisterRequest();
        request.setEmail("test@mail.ru");
        request.setName("test");
        request.setPassword("123MoooasWP3");
        request.setPhone("+79229292999");

        when(userRepository.existsByEmail("test@mail.ru")).thenReturn(true);

        assertThrows(RegisterException.class, () -> {
            authService.register(request);
        });
    }
}