package burunzhuy.repository;

import burunzhuy.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
//    File findByTest(Long ownerId, Pageable pageable);
}
