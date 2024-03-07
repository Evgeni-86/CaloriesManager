package ru.caloriesmanager.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.caloriesmanager.entity.User;
import ru.caloriesmanager.repository.UserRepository;



@NoArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        User user = userRepository.getByEmail(usernameOrEmail);

        if (user == null) {
            throw new UsernameNotFoundException("User not exists by Username or Email");
        }

        return new CustomUserDetails(user);
    }
}