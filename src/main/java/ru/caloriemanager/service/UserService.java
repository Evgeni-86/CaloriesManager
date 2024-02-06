package ru.caloriemanager.service;

import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.caloriemanager.entity.AbstractNamedEntity;
import ru.caloriemanager.entity.User;
import ru.caloriemanager.repository.UserRepository;
import ru.caloriemanager.util.exception.NotFoundException;

import java.util.Comparator;
import java.util.List;
import static ru.caloriemanager.util.ValidationUtil.*;

@Service
public class UserService {
    @Autowired
    public UserRepository repository;

    public User create(@NonNull User user) {
        checkNew(user);
        return repository.save(user);
    }

    public void update(@NonNull User user) throws NotFoundException {
        checkNotFoundWithId(repository.save(user), user.getId());
    }

    public void delete(@Min(1) int id) throws NotFoundException {
        boolean isDeleted = repository.delete(id);
        checkNotFoundWithId(isDeleted, id);
    }

    public User get(@Min(1) int id) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id), id);
    }

    public User getByEmail(@NonNull String email) throws NotFoundException {
        return checkNotFound(repository.getByEmail(email), "email=" + email);
    }

    public List<User> getAll() {
        return repository.getAll().stream().sorted(Comparator.comparing(AbstractNamedEntity::getName)).toList();
    }
}