package ru.caloriemanager.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.caloriemanager.model.User;
import ru.caloriemanager.service.UserService;

import java.util.List;

@Controller
public class AdminRestController extends AbstractUserController {

    @Override
    public User create(User user) {
        return super.create(user);
    }

    @Override
    public void update(User user, int id) {
        super.update(user, id);
    }

    @Override
    public User get(int id) {
        return super.get(id);
    }

    @Override
    public void delete(int id) {
        super.delete(id);
    }

    @Override
    public User getByMail(String email) {
        return super.getByMail(email);
    }

    @Override
    public List<User> getAll() {
        return super.getAll();
    }
}