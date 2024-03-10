package ru.caloriesmanager.web;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import ru.caloriesmanager.entity.User;
import ru.caloriesmanager.model.UserViewModel;
import ru.caloriesmanager.service.CustomUserDetails;
import ru.caloriesmanager.service.CustomUserDetailsService;
import ru.caloriesmanager.service.UserService;
import ru.caloriesmanager.util.exception.NotFoundException;
import java.time.ZoneId;


@RequestMapping("/")
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @RequestMapping("/")
    public String index(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("authenticated", "true");
        }
        return "index";
    }

    @GetMapping("/customLogin")
    public String customLogin() {
        return "custom-login";
    }

    @RequestMapping(value = "/loginFailed")
    public String loginError(Model model) {
        model.addAttribute("error", "true");
        return "custom-login";
    }

    @GetMapping(value = "/logout")
    public String logout(SessionStatus session) {
        SecurityContextHolder.getContext().setAuthentication(null);
        session.setComplete();
        return "redirect:/";
    }

    @PostMapping(value = "/postLogin")
    public String postLogin(HttpSession session, @RequestParam("timeZone") String timeZone) {

        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        validatePrinciple(authentication.getPrincipal());
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        customUserDetails.setZoneId(ZoneId.of(timeZone));

        return "redirect:/";
    }
    private void validatePrinciple(Object principal) {
        if (!(principal instanceof CustomUserDetails)) {
            throw new  IllegalArgumentException("Principal can not be null!");
        }
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new UserViewModel());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("user") UserViewModel userViewModel,
                          @RequestParam("passwordConfirm") String passwordConfirm,
                          Model model) {

        if (!userViewModel.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "Passwords don't match");
            return "registration";
        }

        try {
            userService.getByEmail(userViewModel.getEmail());
            model.addAttribute("emailError", "A user with that name already exists");
            return "registration";
        } catch (NotFoundException exception) {

            userViewModel.setPassword(passwordEncoder.encode(userViewModel.getPassword()));
            User user = UserViewModel.getUserInstance(userViewModel);
            userService.create(user);

            return "redirect:/custom-login";
        }
    }

}
