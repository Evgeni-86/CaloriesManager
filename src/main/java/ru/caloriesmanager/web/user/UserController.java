package ru.caloriesmanager.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.caloriesmanager.entity.User;
import ru.caloriesmanager.service.CustomUserDetailsService;
import ru.caloriesmanager.service.UserService;
import ru.caloriesmanager.model.UserViewModel;
import java.time.ZoneId;
import java.time.ZonedDateTime;


@RequestMapping("/")
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/users")
    public String getUser(Model model) {
        int userId = CustomUserDetailsService.getCustomUserDetails().getUser().getId();

        User user = userService.get(userId);

        ZonedDateTime systemZoned = ZonedDateTime.of(user.getRegistered(), ZoneId.systemDefault());
        ZonedDateTime userZoned = systemZoned.withZoneSameInstant(CustomUserDetailsService.getCustomUserDetails().getZoneId());
        user.setRegistered(userZoned.toLocalDateTime());

        model.addAttribute("user", UserViewModel.getModel(user));
        return "users";
    }
}
