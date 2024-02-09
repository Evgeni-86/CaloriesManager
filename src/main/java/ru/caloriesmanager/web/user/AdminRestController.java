package ru.caloriesmanager.web.user;

import org.springframework.stereotype.Controller;
import ru.caloriesmanager.model.User;

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