package ru.caloriesmanager.web;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import ru.caloriesmanager.entity.User;
import ru.caloriesmanager.model.form.RegistrationForm;
import ru.caloriesmanager.service.CustomUserDetails;
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
    public String postLogin(@RequestParam("timeZone") String timeZone) {

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
        model.addAttribute("user", new RegistrationForm());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@Valid @ModelAttribute("user") RegistrationForm registrationForm,
                          BindingResult bindingResult,
                          @RequestParam("passwordConfirm") String passwordConfirm,
                          Model model) {

        if (bindingResult.hasErrors()){
            model.addAttribute("error", "true");
            return "registration";
        }

        if (!registrationForm.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "true");
            return "registration";
        }

        try {
            userService.getByEmail(registrationForm.getEmail());
            model.addAttribute("emailError", "true");
            return "registration";
        } catch (NotFoundException exception) {
            registrationForm.setPassword(passwordEncoder.encode(registrationForm.getPassword()));

            User user = RegistrationForm.getUserInstance(registrationForm);
            userService.create(user);

            return "redirect:/custom-login";
        }
    }

}
