package ru.caloriemanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.caloriemanager.model.AbstractNamedEntity;
import ru.caloriemanager.model.User;
import ru.caloriemanager.repository.UserRepository;
import ru.caloriemanager.util.exception.NotFoundException;

import java.util.Comparator;
import java.util.List;

import static ru.caloriemanager.util.ValidationUtil.*;

@Service
public class UserServiceCopy {
    @Qualifier("inMemoryUserRepo")
    @Autowired
    public UserRepository repository;

    public User create(User user) {
        if (user == null) throw new IllegalArgumentException("not valid arguments");
        checkNew(user);
        return repository.save(user);
    }

    public void update(User user) throws NotFoundException {
        if (user == null) throw new IllegalArgumentException("not valid arguments");
        checkNotFoundWithId(repository.save(user), user.getId());
    }

    public void delete(int id) throws NotFoundException {
        if (id < 1) throw new IllegalArgumentException("not valid arguments");
        boolean isDeleted = repository.delete(id);
        checkNotFoundWithId(isDeleted, id);
    }

    public User get(int id) throws NotFoundException {
        if (id < 1) throw new IllegalArgumentException("not valid arguments");
        return checkNotFoundWithId(repository.get(id), id);
    }

    public User getByEmail(String email) throws NotFoundException {
        if (email == null) throw new IllegalArgumentException("not valid arguments");
        return checkNotFound(repository.getByEmail(email), "email=" + email);
    }

    public List<User> getAll() {
        return repository.getAll().stream().sorted(Comparator.comparing(AbstractNamedEntity::getName)).toList();
    }
}