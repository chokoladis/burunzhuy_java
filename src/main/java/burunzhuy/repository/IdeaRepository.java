package burunzhuy.repository;

import burunzhuy.entity.Idea;
import burunzhuy.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdeaRepository extends JpaRepository<Idea, Long> {
    Page<Idea> findByOwnerId(Long ownerId, Pageable pageable);
}
