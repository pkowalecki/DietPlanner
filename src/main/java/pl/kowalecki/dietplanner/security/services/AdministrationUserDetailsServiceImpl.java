package pl.kowalecki.dietplanner.security.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.kowalecki.dietplanner.model.AdministrationUser;
import pl.kowalecki.dietplanner.repository.AdministrationUserRepository;

@Service
public class AdministrationUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    AdministrationUserRepository administrationUserRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AdministrationUser user = administrationUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return AdministrationUserDetailsImpl.build(user);
    }

}