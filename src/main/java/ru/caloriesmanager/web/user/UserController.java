package ru.caloriesmanager.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.caloriesmanager.model.User;
import ru.caloriesmanager.service.UserService;


@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/")
    public String index(Model model) {
        return "index";
    }

    @RequestMapping(value = "/userss")
    public String getAllUsers(Model model) {
        return "index2";
    }

    @RequestMapping(value = "/userss/{id}")
    public String getUser(@PathVariable int id, Model model) {
        User user = userService.get(id);
        model.addAttribute("user", user);
        return "";
    }
}
