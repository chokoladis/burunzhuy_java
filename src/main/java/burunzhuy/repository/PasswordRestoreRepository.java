package burunzhuy.repository;

import burunzhuy.entity.auth.PasswordRestore;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface PasswordRestoreRepository extends JpaRepository<PasswordRestore, Long> {
    // todo get qty trying
    Collection<PasswordRestore> findByUserIdAndCreatedAtAfter(Long userId, LocalDateTime timeBefore);
    Optional<PasswordRestore> findOneByToken(String token);
}
