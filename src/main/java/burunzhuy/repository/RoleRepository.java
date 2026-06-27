package burunzhuy.repository;

import burunzhuy.entity.Role;
import burunzhuy.enums.user.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);

    Set<Role> findByNameIn(Set<RoleEnum> names);
}
