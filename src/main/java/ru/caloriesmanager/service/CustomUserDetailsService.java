package ru.caloriesmanager.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.caloriesmanager.entity.User;
import ru.caloriesmanager.repository.UserRepository;



@NoArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

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

    public static CustomUserDetails getCustomUserDetails() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        validatePrinciple(authentication.getPrincipal());
        return (CustomUserDetails) authentication.getPrincipal();
    }
    private static void validatePrinciple(Object principal) {
        if (!(principal instanceof CustomUserDetails)) {
            throw new  IllegalArgumentException("Principal can not be null!");
        }
    }
}