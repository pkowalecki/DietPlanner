package pl.kowalecki.dietplanner.services;

import pl.kowalecki.dietplanner.model.DTO.RegistrationRequestDTO;
import pl.kowalecki.dietplanner.model.Role;
import pl.kowalecki.dietplanner.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    User createUser(RegistrationRequestDTO registrationRequest);

    Set<Role> setUserRoles(List<String> roles);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    User findUserByHash(String hash);

    void registerUser(User user);

    void save(User user);

    Optional<User> findById(int id);
}
