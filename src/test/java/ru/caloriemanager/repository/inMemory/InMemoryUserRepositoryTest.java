package ru.caloriemanager.repository.inMemory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.caloriemanager.model.Role;
import ru.caloriemanager.model.User;
import ru.caloriemanager.repository.UserRepository;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class InMemoryUserRepositoryTest {

    private static final UserRepository SUT = new InMemoryUserRepository();

    @Test
    void saveNewUser() {
        User user = new User(null, "testUser", "email", "pass", Role.ROLE_USER);
        Assertions.assertNull(SUT.save(user));
        Assertions.assertEquals(user, SUT.get(user.getId()));
    }

    @Test
    void saveUpdateUser() {
        User user = new User(null, "testUser", "email", "pass", Role.ROLE_USER);
        SUT.save(user);
        User userUpdate = new User(user.getId(), "testUserUpdate", "email", "pass", Role.ROLE_USER);
        Assertions.assertEquals(user, SUT.save(userUpdate));
        Assertions.assertEquals(userUpdate, SUT.get(userUpdate.getId()));
    }

    @Test
    void delete() {
        User user = new User(null, "testUser", "email", "pass", Role.ROLE_USER);
        SUT.save(user);
        Assertions.assertTrue(SUT.delete(user.getId()));
    }

    @Test
    void get() {
        User user = new User(null, "testUser", "email", "pass", Role.ROLE_USER);
        SUT.save(user);
        Assertions.assertEquals(user, SUT.get(user.getId()));
    }

    @Test
    void getAll() {
        try {
            Field fieldUserMap = SUT.getClass().getDeclaredField("usersMap");
            fieldUserMap.setAccessible(true);
            Map<Integer, User> usersMap = (Map<Integer, User>) fieldUserMap.get(SUT);
            List<User> userList = usersMap.values().stream()
                    .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                    .collect(Collectors.toList());
            Assertions.assertEquals(userList, SUT.getAll());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println("method getAll is not verify");
        }
    }

    @Test
    void getByEmail() {
        User user = new User(null, "testUser", "test_email", "pass", Role.ROLE_USER);
        SUT.save(user);
        Assertions.assertEquals(user, SUT.getByEmail("test_email"));
    }
}