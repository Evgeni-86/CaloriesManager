package ru.caloriemanager.web.user;

import org.springframework.stereotype.Controller;
import ru.caloriemanager.entity.User;

import static ru.caloriemanager.web.SecurityUtil.authUserId;

@Controller
public class ProfileRestController extends AbstractUserController {

    public User get() {
        return super.get(authUserId());
    }

    public void delete() {
        super.delete(authUserId());
    }

    public void update(User user) {
        super.update(user, authUserId());
    }
}