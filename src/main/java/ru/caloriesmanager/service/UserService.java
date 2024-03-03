package ru.caloriesmanager.service;

import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.caloriesmanager.entity.User;
import ru.caloriesmanager.repository.UserRepository;
import ru.caloriesmanager.util.exception.NotFoundException;
import java.util.List;
import static ru.caloriesmanager.util.ValidationUtil.*;

@Service
public class UserService {
    @Autowired
    public UserRepository repository;

    @CacheEvict(value = "userCache", allEntries = true)
    public User create(@NonNull User user) {
        checkNew(user);
        return repository.save(user);
    }

    @CacheEvict(value = "userCache", allEntries = true)
    public void update(@NonNull User user) throws NotFoundException {
        checkNotFoundWithId(repository.save(user), user.getId());
    }

    @CacheEvict(value = "userCache", allEntries = true)
    public void delete(@Min(1) int id) throws NotFoundException {
        boolean isDeleted = repository.delete(id);
        checkNotFoundWithId(isDeleted, id);
    }

    @Cacheable(value = "userCache")
    public User get(@Min(1) int id) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id), id);
    }

    @Cacheable(value = "userCache")
    public User getByEmail(@NonNull String email) throws NotFoundException {
        return checkNotFound(repository.getByEmail(email), "email=" + email);
    }

    @Cacheable(value = "userCache")
    public List<User> getAll() {
        return repository.getAll();
    }
}