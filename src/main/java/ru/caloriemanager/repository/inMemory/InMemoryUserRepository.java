package ru.caloriemanager.repository.inMemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.caloriemanager.model.User;
import ru.caloriemanager.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Repository
public class InMemoryUserRepository extends InMemoryBaseRepository<User> implements UserRepository {

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private static Map<Integer, User> usersMap = new ConcurrentHashMap<>();
    private static AtomicInteger counter = new AtomicInteger(0);

    {
        if (usersMap.isEmpty())
            DataForUsersMockRepository.USERS_LIST.forEach(el -> save(el));
    }

    @Override
    public User save(User user) {
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            LOG.info("save {}", user);
        } else
            LOG.info("update {}", user);
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
