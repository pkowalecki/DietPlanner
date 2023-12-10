package pl.kowalecki.dietplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kowalecki.dietplanner.model.AdministrationUser;
import pl.kowalecki.dietplanner.model.Meal;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdministrationUserRepository extends JpaRepository<AdministrationUser, Integer> {

    Optional<AdministrationUser> findByEmail(String email);
    Boolean existsAdministrationUserByEmail(String email);

}