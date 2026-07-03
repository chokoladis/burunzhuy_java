package burunzhuy.service;

import burunzhuy.entity.user.User;
import burunzhuy.exception.auth.UserException;
import burunzhuy.repository.UserRepository;
import burunzhuy.service.user.ProfileService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private UserRepository userRepository;

    private ProfileService profileService;
    private User mockUser;

    @BeforeEach
    void setUp() {
        profileService = new ProfileService(userRepository);

        mockUser = new User();
        mockUser.setEmail("test@mail.ru");
        mockUser.setPassword("test123");

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(mockUser.getEmail());

        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getCurrentUserSuccess() {
        when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(this.mockUser);

        assertEquals(this.mockUser, profileService.getCurrentUser());
    }

    @Test
    void getCurrentUserErrorNotFound() {
        when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(null);

        assertThrows(UserException.class, () -> {
            profileService.getCurrentUser();
        });
    }
}