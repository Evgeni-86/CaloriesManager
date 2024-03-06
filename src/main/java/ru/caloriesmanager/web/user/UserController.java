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

    @RequestMapping("/users")
    public String getUser(Model model) {
        User user = userService.get(SecurityUtil.authUserId());
        model.addAttribute("user", UserViewModel.getModel(user));
        return "users";
    }
}
