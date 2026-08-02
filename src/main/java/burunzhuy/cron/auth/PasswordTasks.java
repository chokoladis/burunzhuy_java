package burunzhuy.cron.auth;

import burunzhuy.entity.auth.PasswordRestore;
import burunzhuy.repository.PasswordRestoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class PasswordTasks {

    private final PasswordRestoreRepository passwordRestoreRepository;

    @Scheduled(cron = "0 0 2 * * *")
    public void deleteOld()
    {
        Collection<PasswordRestore> passwordRestores = passwordRestoreRepository.findByCreatedAtBefore(LocalDateTime.now().minusDays(2L));
        if (passwordRestores.isEmpty())
            return;

        passwordRestoreRepository.deleteAllInBatch(passwordRestores);
    }
}
