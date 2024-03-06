package ru.caloriesmanager.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.caloriesmanager.entity.User;
import ru.caloriesmanager.model.UserViewModel;
import ru.caloriesmanager.service.UserService;
import ru.caloriesmanager.util.exception.NotFoundException;


@RequestMapping("/")
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/customLogin")
    public String customLogin() {
        return "custom-login";
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
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "registration";
        }

        try {
            userService.getByEmail(userViewModel.getEmail());
            model.addAttribute("emailError", "Пользователь с таким именем уже существует");
            return "registration";
        } catch (NotFoundException exception) {

            userViewModel.setPassword(passwordEncoder.encode(userViewModel.getPassword()));
            User user = UserViewModel.getUserInstance(userViewModel);
            User newUser = userService.create(user);
            model.addAttribute("user", newUser);

            SecurityUtil.setUserId(newUser.getId());
            return "custom-login";
        }
    }

}
