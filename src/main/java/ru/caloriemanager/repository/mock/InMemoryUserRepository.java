package ru.caloriemanager.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.caloriemanager.model.Role;
import ru.caloriemanager.model.User;
import ru.caloriemanager.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Repository
public class InMemoryUserRepository extends InMemoryBaseRepository<User> implements UserRepository {

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private Map<Integer, User> usersMap = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);
    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;

    {
        save(new User(null, "User_1", "user_email@mail.ru", "user_pass", Role.ROLE_USER));
        save(new User(null, "Admin", "admin_email@mail.ru", "admin_pass", Role.ROLE_ADMIN));
    }

    public int getCountUser() {
        return usersMap.size();
    }

    @Override
    public User save(User user) {
        LOG.info("save {}", user);
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
        }
        return usersMap.put(user.getId(), user);
    }


    @Override
    public boolean delete(int id) {
        LOG.info("delete {}", id);
        return usersMap.remove(id) != null;
    }

    @Override
    public User get(int id) {
        LOG.info("get {}", id);
        return usersMap.get(id);
    }

    @Override
    public List<User> getAll() {
        LOG.info("getAll");
        return usersMap.values().stream()
                .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        Objects.requireNonNull(email);
        LOG.info("getByEmail {}", email);
        return usersMap.values().stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst()
                .orElse(null);
    }
}
