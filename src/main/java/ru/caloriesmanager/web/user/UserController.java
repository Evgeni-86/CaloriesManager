package ru.caloriesmanager.web.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.caloriesmanager.entity.User;
import ru.caloriesmanager.service.CustomUserDetailsService;
import ru.caloriesmanager.service.UserService;
import ru.caloriesmanager.model.UserViewModel;

import java.time.ZoneId;
import java.time.ZonedDateTime;


@RequestMapping("user")
@Controller
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping("/profile")
    public String getUser(Model model) {
        int userId = CustomUserDetailsService.getCustomUserDetails().getUser().getId();
        User user = userService.get(userId);

        ZonedDateTime systemZoned = ZonedDateTime.of(user.getRegistered(), ZoneId.systemDefault());
        ZonedDateTime userZoned = systemZoned.withZoneSameInstant(CustomUserDetailsService.getCustomUserDetails().getZoneId());
        user.setRegistered(userZoned.toLocalDateTime());

        model.addAttribute("user", UserViewModel.getModel(user));
        return "users";
    }

    @RequestMapping("/update")
    public String updateUser(Model model) {
        User user = CustomUserDetailsService.getCustomUserDetails().getUser();
        model.addAttribute("user", UserViewModel.getModel(user));
        return "userForm";
    }

    @RequestMapping("/edit")
    public String editUser(@Valid @ModelAttribute("user") UserViewModel userViewModel,
                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "userForm";
        }

        User user = CustomUserDetailsService.getCustomUserDetails().getUser();
        user.setName(userViewModel.getName());
        user.setCaloriesPerDay(userViewModel.getCaloriesPerDay());
        userService.update(user);
        return "redirect:/user/profile";
    }
}
