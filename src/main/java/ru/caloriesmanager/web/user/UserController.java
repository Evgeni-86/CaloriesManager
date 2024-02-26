package ru.caloriesmanager.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.caloriesmanager.entity.User;
import ru.caloriesmanager.service.UserService;
import ru.caloriesmanager.model.UserViewModel;
import ru.caloriesmanager.web.SecurityUtil;


@RequestMapping("/")
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/")
    public String index(Model model) {
        return "index";
    }

    @RequestMapping(value = "/users")
    public String getUser(@RequestParam("select_user") int userId, Model model) {
        User user = userService.get(userId);
        SecurityUtil.setUserId(userId);
        model.addAttribute("user", UserViewModel.getModel(user));
        return "users";
    }
}
