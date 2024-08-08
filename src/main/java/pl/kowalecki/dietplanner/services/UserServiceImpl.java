package pl.kowalecki.dietplanner.services;

import lombok.AllArgsConstructor;
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
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private UserRepository userRepository;

    public User createUser(RegistrationRequestDTO registrationRequest) {
        return new User(
                registrationRequest.getName() != null ? registrationRequest.getName() : "",
                registrationRequest.getNickname(),
                registrationRequest.getSurname() != null ? registrationRequest.getSurname() : "",
                registrationRequest.getEmailReg(),
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

    public boolean findAndActivateUserByHash(String hash){
        User user = userRepository.findUserByHash(hash);
        if (user!=null && user.getHash().equals(hash) && !user.isActive()){
            user.setActive(true);
            userRepository.save(user);
            System.out.println("Aktywowano użytkownika: " + user.getId());
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    @Override
    public User findUserByHash(String hash) {
        return userRepository.findUserByHash(hash);
    }

    @Override
    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @Override
    public void save(User user){
        userRepository.save(user);
    }

    @Override
    public Optional<User> findById(int id){
        return userRepository.findById(id);
    }
}
