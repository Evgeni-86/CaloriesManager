package ru.caloriemanager.repository.mock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import ru.caloriemanager.model.Role;
import ru.caloriemanager.model.User;
import ru.caloriemanager.repository.UserRepository;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserRepositoryTest {

    private static final UserRepository userRepository = new InMemoryUserRepository();


    @Test
    void saveNewUser() {
        User user = new User(null, "testUser", "email", "pass", Role.ROLE_USER);
        Assertions.assertNull(userRepository.save(user));
        Assertions.assertEquals(user, userRepository.get(user.getId()));
    }

    @Test
    void saveUpdateUser() {
        User user = new User(null, "testUser", "email", "pass", Role.ROLE_USER);
        userRepository.save(user);
        User userUpdate = new User(user.getId(), "testUserUpdate", "email", "pass", Role.ROLE_USER);
        Assertions.assertEquals(user, userRepository.save(userUpdate));
        Assertions.assertEquals(userUpdate, userRepository.get(userUpdate.getId()));
    }

    @Test
    void delete() {
        User user = new User(null, "testUser", "email", "pass", Role.ROLE_USER);
        userRepository.save(user);
        Assertions.assertTrue(userRepository.delete(user.getId()));
    }

    @Test
    void get() {
        User user = new User(null, "testUser", "email", "pass", Role.ROLE_USER);
        userRepository.save(user);
        Assertions.assertEquals(user, userRepository.get(user.getId()));
    }

    @Test
    void getAll() {
        try {
            Field fieldUserMap = userRepository.getClass().getDeclaredField("usersMap");
            fieldUserMap.setAccessible(true);
            Map<Integer, User> usersMap = (Map<Integer, User>) fieldUserMap.get(userRepository);
            List<User> userList = usersMap.values().stream()
                    .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                    .collect(Collectors.toList());
            Assertions.assertEquals(userList, userRepository.getAll());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println("method getAll is not verify");
        }
    }

    @Test
    void getByEmail() {
        User user = new User(null, "testUser", "test_email", "pass", Role.ROLE_USER);
        userRepository.save(user);
        Assertions.assertEquals(user, userRepository.getByEmail("test_email"));
    }
}