package ru.caloriemanager.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.caloriemanager.model.User;
import ru.caloriemanager.service.UserService;

import java.util.List;

import static ru.caloriemanager.util.ValidationUtil.assureIdConsistent;
import static ru.caloriemanager.util.ValidationUtil.checkNew;

public abstract class AbstractUserController {
    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService service;

    public List<User> getAll() {
        LOG.info("getAll");
        return service.getAll();
    }

    public User get(int id) {
        LOG.info("get {}", id);
        return service.get(id);
    }

    public User create(User user) {
        LOG.info("create {}", user);
        checkNew(user);
        return service.create(user);
    }

    public void delete(int id) {
        LOG.info("delete {}", id);
        service.delete(id);
    }

    public void update(User user, int id) {
        LOG.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        service.update(user);
    }

    public User getByMail(String email) {
        LOG.info("getByEmail {}", email);
        return service.getByEmail(email);
    }
}