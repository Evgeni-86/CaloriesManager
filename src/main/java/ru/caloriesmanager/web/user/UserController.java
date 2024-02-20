package ru.caloriesmanager.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class UserController {
    @RequestMapping(value = "/")
    public String getAll(Model model) {
        return "index";
    }
}
