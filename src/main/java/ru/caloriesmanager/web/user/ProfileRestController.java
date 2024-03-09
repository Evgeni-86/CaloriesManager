package ru.caloriesmanager.web.user;

import org.springframework.stereotype.Controller;
import ru.caloriesmanager.entity.User;
import ru.caloriesmanager.service.CustomUserDetailsService;


@Controller
public class ProfileRestController extends AbstractUserController {

    public User get() {
        int userId = CustomUserDetailsService.getCustomUserDetails().getUser().getId();
        return super.get(userId);
    }

    public void delete() {
        int userId = CustomUserDetailsService.getCustomUserDetails().getUser().getId();
        super.delete(userId);
    }

    public void update(User user) {
        int userId = CustomUserDetailsService.getCustomUserDetails().getUser().getId();
        super.update(user, userId);
    }
}