package pl.kowalecki.dietplanner.controller.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kowalecki.dietplanner.exception.RegistrationException;
import pl.kowalecki.dietplanner.model.DTO.RegistrationRequestDTO;
import pl.kowalecki.dietplanner.model.Role;
import pl.kowalecki.dietplanner.model.enums.EnumRole;
import pl.kowalecki.dietplanner.repository.RoleRepository;
import pl.kowalecki.dietplanner.security.services.UserDetailsServiceImpl;
import pl.kowalecki.dietplanner.utils.TextTools;

import java.util.*;

@Component
public class RegisterHelper {


    private UserDetailsServiceImpl userService;
    private RoleRepository roleRepository;

    @Autowired
    public RegisterHelper(UserDetailsServiceImpl userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    static Map<String, String> errors;

    public Map<String, String> checkRegistrationData(RegistrationRequestDTO registrationRequest) {
        errors = new HashMap<>();
        checkNickname(registrationRequest.getNickname());
        checkUserName(registrationRequest.getName());
        checkSurname(registrationRequest.getSurname());
        checkEmail(registrationRequest.getEmailReg());
        checkPassword(registrationRequest.getPasswordReg(), registrationRequest.getPasswordReg2());
        return errors;
    }

    private void checkNickname(String nickname) {
        if (!TextTools.isTextLengthOk(nickname, 5, 50)) {
            errors.put(RegisterPole.NICKNAME.getFieldName(), "Length must be between 5-50");
        }
    }

    private void checkUserName(String userName) {
        if (!TextTools.isTextLengthOk(userName, 5, 50)) {
            errors.put(RegisterPole.NAME.getFieldName(), "Length must be between 5-50");
        }
    }

    private void checkSurname(String surname) {
        if (!TextTools.isTextLengthOk(surname, 5, 50)) {
            errors.put(RegisterPole.SURNAME.getFieldName(), "Length must be between 5-50");
        }
    }

    private void checkEmail(String email) {
        if (!TextTools.isTextLengthOk(email, 5, 50)) {
            errors.put(RegisterPole.EMAIL.getFieldName(), "Length must be between 5-50");
            return;
        }
        if (userService.existsUserByEmail(email)) errors.put(RegisterPole.EMAIL.getFieldName(), "Email address already exists");
    }

    private void checkPassword(String password, String password2) {
        if (!password.equals(password2)) {
            errors.put(RegisterPole.PASSWORD2.getFieldName(), "Password not mach");
            return;
        }
        if (!TextTools.passwordPatternValidate(password))
            errors.put(RegisterPole.PASSWORD.getFieldName(), "Pattern not match");
    }


    public Set<Role> setUserRole(List<String> role) {
        Set<Role> roles = new HashSet<>();
        for (String singleRole: role) {
            if (singleRole == null) {
                Role userRole = roleRepository.findByName(EnumRole.ROLE_USER)
                        .orElseThrow(() -> new RegistrationException("Role user not found!"));
                roles.add(userRole);
            } else {
                roles.add(getRoleFromString(singleRole));
            }
        }
        return roles;
    }

    //FIXME fix return value
    private Role getRoleFromString(String role) {
        Optional<Role> roleEn = switch (role) {
            case "ROLE_ADMIN" -> roleRepository.findByName(EnumRole.ROLE_ADMIN);
            case "ROLE_USER" -> roleRepository.findByName(EnumRole.ROLE_USER);
            default -> roleRepository.findByName(EnumRole.ROLE_USER);
        };
        if (roleEn!= null && roleEn.isPresent()) return roleEn.get();
        return new Role();
    }
}
