package burunzhuy.service;

import burunzhuy.dto.idea.CreateRequest;
import burunzhuy.entity.File;
import burunzhuy.entity.Idea;
import burunzhuy.entity.User;
import burunzhuy.exception.auction.AccessException;
import burunzhuy.exception.common.EntityNotFound;
import burunzhuy.repository.IdeaRepository;
import burunzhuy.repository.UserRepository;
import burunzhuy.resource.idea.FullResource;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IdeaServiceTest {

    @Mock
    private IdeaRepository ideaRepository;
    @Mock
    private ProfileService profileService;
    @Mock
    private FileService fileService;
    @Mock
    private UserRepository userRepository;

    private IdeaService ideaService;
    private Page<Idea> mockIdeas;
    private Faker faker;

    @BeforeEach
    public void setUp() {
        faker = new Faker();

        ideaService = new IdeaService(
                ideaRepository,
                profileService,
                fileService
        );

        mockIdeas = new PageImpl<>(List.of(this.getRandom(), this.getRandom()));
    }

    private Idea getRandom() {
        return getRandom(null, null);
    }

    private Idea getRandom(Long ideaId, User owner) {
        var random = new Random();
        Idea newIdea = new Idea();

        if (ideaId != null) {
            newIdea.setId(ideaId);
        }
        if (owner != null) {
            newIdea.setOwner(owner);
        }
        newIdea.setTitle(faker.text().text(30));
        newIdea.setShortDescription(faker.text().text(100));
        newIdea.setPriceMin(BigDecimal.valueOf(random.nextFloat(5000)));
        newIdea.setPriceInstanceBuy(BigDecimal.valueOf(random.nextFloat(10000000)));

        File newFile = new File();
        newFile.setOriginalName(faker.file().fileName());
        newFile.setExt(faker.file().extension());
        newFile.setPath("ideas/" + newFile.getOriginalName());

        newIdea.setPreview(newFile);
        newIdea.setFullDescription(faker.text().text(100, 500));

        return newIdea;
    }

    @Test
    public void listForCurrentUserSuccess() {
        Integer page = 0;
        Integer perPage = 10;
        Long userId = 1L;
        Pageable pageable = PageRequest.of(page, perPage, Sort.by("createdAt").descending());

        when(profileService.getCurrentUserId()).thenReturn(userId);
        when(ideaRepository.findByOwnerId(userId, pageable)).thenReturn(mockIdeas);

        var result = ideaService.getForCurrentUser(page, perPage);
        assertNotNull(result);
        assertEquals(mockIdeas.getContent().size(), result.getContent().size());
    }


    @Test
    public void getByIdSuccess() {
        Long userId = 1L;
        Long ideaId = 3L;

        var user = new User();
        user.setId(userId);

        Idea ideaById = getRandom(ideaId, user);

        when(profileService.getCurrentUserId()).thenReturn(userId);
        when(ideaRepository.findById(ideaId)).thenReturn(Optional.of(ideaById));

        assertThat(ideaService.getById(ideaId)).isEqualTo(new FullResource(ideaById));
    }

    @Test
    public void getByIdErrorAccess() {
        Long userId = 1L;
        Long ideaId = 3L;

        var user = new User();
        user.setId(99L);

        Idea ideaById = getRandom(ideaId, user);

        when(profileService.getCurrentUserId()).thenReturn(userId);
        when(ideaRepository.findById(ideaId)).thenReturn(Optional.of(ideaById));

        assertThrows(AccessException.class, () -> ideaService.getById(ideaId));
    }

    @Test
    public void getByIdErrorNotFound() {
        Long ideaId = 3L;

        when(ideaRepository.findById(ideaId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFound.class, () -> ideaService.getById(ideaId));
    }

    @Test
    public void createSuccess() {
        var preview = new MockMultipartFile(
                "testfile",
                "testing_file.txt",
                "text/plane",
                "random data".getBytes()
        );
        MultipartFile[] attaches = new MultipartFile[]{preview};

        var user = new User();
        user.setId(1L);

        when(profileService.getCurrentUser()).thenReturn(user);
        when(fileService.save(preview, "ideas")).thenReturn(new File());
        when(ideaRepository.save(any(Idea.class))).thenAnswer(invocation -> {
            return invocation.getArguments()[0];
        });

        FullResource result = ideaService.create(this.prepareCreateRequest(), preview, attaches);

        ArgumentCaptor<Idea> captor = ArgumentCaptor.forClass(Idea.class);
        verify(ideaRepository).save(captor.capture());

        Idea savedIdea = captor.getValue();
        assertEquals(new FullResource(savedIdea), result);
    }

    private CreateRequest prepareCreateRequest() {
        CreateRequest request = new CreateRequest();

        var random = new Random();

        request.setTitle(faker.text().text(30));
        request.setShortDescription(faker.text().text(100));
        request.setPriceMin(BigDecimal.valueOf(random.nextFloat(5000)));
        request.setPriceInstanceBuy(BigDecimal.valueOf(random.nextFloat(10000000)));
        request.setFullDescription(faker.text().text(100, 500));

        return request;
    }
}