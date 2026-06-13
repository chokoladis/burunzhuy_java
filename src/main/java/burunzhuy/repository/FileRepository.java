package burunzhuy.repository;

import burunzhuy.entity.File;
import burunzhuy.entity.Idea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
//    File findByTest(Long ownerId, Pageable pageable);
}
