package ru.caloriesmanager.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.caloriesmanager.entity.User;
import ru.caloriesmanager.service.UserService;
import ru.caloriesmanager.model.UserViewModel;
import ru.caloriesmanager.web.SecurityUtil;


@RequestMapping("/")
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String index(Model model) {
        return "index";
    }

    @RequestMapping("/my-login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/signin")
    public String signin(Model model) {
        model.addAttribute("user", new UserViewModel());
        return "signin";
    }
    @PostMapping("/signin")
    public String signin(@ModelAttribute("user") UserViewModel userViewModel, Model model) {
        User user = UserViewModel.getUserInstance(userViewModel);
        User newUser = userService.create(user);
        model.addAttribute("user", newUser);
        SecurityUtil.setUserId(newUser.getId());
        return "users";
    }

//    @RequestMapping("/users")
//    public String getUser(@RequestParam("select_user") int userId, Model model) {
//        User user = userService.get(userId);
//        SecurityUtil.setUserId(userId);
//        model.addAttribute("user", UserViewModel.getModel(user));
//        return "users";
//    }
}
