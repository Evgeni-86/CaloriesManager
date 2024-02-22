package ru.caloriesmanager.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.caloriesmanager.model.User;
import ru.caloriesmanager.service.UserService;
import ru.caloriesmanager.transferObject.UserTO;


@RequestMapping("/")
@Controller
public class UserController {

    @Autowired
    private ProfileRestController profileRestController;
    @Autowired
    private AdminRestController adminRestController;

    @RequestMapping(value = "/")
    public String index(Model model) {
        return "index";
    }

    @RequestMapping(value = "/users")
    public String getUser(@RequestParam("select_user") int userId,Model model) {
        User user = adminRestController.get(userId);
        model.addAttribute("user", UserTO.getTransferObject(user));
        return "users";
    }
}
