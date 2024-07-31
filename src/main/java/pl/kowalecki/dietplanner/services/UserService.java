package pl.kowalecki.dietplanner.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.kowalecki.dietplanner.exception.RegistrationException;
import pl.kowalecki.dietplanner.model.DTO.RegistrationRequestDTO;
import pl.kowalecki.dietplanner.model.Role;
import pl.kowalecki.dietplanner.model.User;
import pl.kowalecki.dietplanner.model.enums.EnumRole;
import pl.kowalecki.dietplanner.repository.RoleRepository;
import pl.kowalecki.dietplanner.repository.UserRepository;
import pl.kowalecki.dietplanner.utils.TextTools;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private UserRepository userRepository;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public User createUser(RegistrationRequestDTO registrationRequest) {
        return new User(
                registrationRequest.getName() != null ? registrationRequest.getName() : "",
                registrationRequest.getSurname() != null ? registrationRequest.getSurname() : "",
                registrationRequest.getEmailReg(),
                registrationRequest.getNickname(),
                passwordEncoder.encode(registrationRequest.getPasswordReg()),
                false, TextTools.generateActivationHash());
    }

    public Set<Role> setUserRoles(List<String> roles) {
        Set<Role> userRoles = new HashSet<>();
        for (String role : roles) {
            userRoles.add(getRoleFromString(role));
        }
        return userRoles;
    }

    private Role getRoleFromString(String role) {
        return switch (role) {
            case "ROLE_ADMIN" -> roleRepository.findByName(EnumRole.ROLE_ADMIN)
                    .orElseThrow(() -> new RegistrationException("Role admin not found!"));
            case "ROLE_USER" -> roleRepository.findByName(EnumRole.ROLE_USER)
                    .orElseThrow(() -> new RegistrationException("Role user not found!"));
            default -> throw new RegistrationException("Invalid role: " + role);
        };
    }

    public void registerUser(User user) {
        userRepository.save(user);
    }
}
