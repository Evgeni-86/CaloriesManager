package ru.caloriemanager.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.caloriemanager.model.User;
import ru.caloriemanager.repository.UserRepository;
import ru.caloriemanager.web.UserTestData;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.caloriemanager.web.UserTestData.ADMIN;
import static ru.caloriemanager.web.UserTestData.USER;


public class MockUserRepository implements UserRepository {

    private static final Logger LOG = LoggerFactory.getLogger(MockUserRepository.class);
    private Map<Integer, User> usersMap = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);
    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;

    public void init() {
        usersMap.clear();
        usersMap.put(UserTestData.USER_ID, USER);
        usersMap.put(UserTestData.ADMIN_ID, ADMIN);
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
