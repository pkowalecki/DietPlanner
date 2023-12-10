package pl.kowalecki.dietplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kowalecki.dietplanner.model.Role;
import pl.kowalecki.dietplanner.model.enums.EnumRole;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(EnumRole name);
}

