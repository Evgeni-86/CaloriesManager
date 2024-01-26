package ru.caloriemanager.repository.mock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.caloriemanager.model.Meal;
import ru.caloriemanager.model.Role;
import ru.caloriemanager.model.User;
import ru.caloriemanager.repository.MealRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;


class InMemoryMealRepositoryTest {

    private static final MealRepository mealRepository = new InMemoryMealRepository();
    private static int userId;

    @BeforeAll
    static void init() {
        userId = mealRepository.getCountUsers();
    }

    @BeforeEach
    void incrementUserId() {
        ++userId;
    }

    @Test
    void save() {
        User user = new User(userId, "testUser", "email", "pass", Role.ROLE_USER);
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        Assertions.assertEquals(meal, mealRepository.save(meal, user.getId()));
        Assertions.assertEquals(meal, mealRepository.get(meal.getId(), user.getId()));
    }

    @Test
    void update() {
        User user = new User(userId, "testUser", "email", "pass", Role.ROLE_USER);
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        Meal mealUpdate = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 1000);
        Assertions.assertEquals(meal, mealRepository.save(meal, user.getId()));
        Assertions.assertEquals(mealUpdate, mealRepository.save(mealUpdate, user.getId()));
        Assertions.assertEquals(mealUpdate, mealRepository.get(mealUpdate.getId(), user.getId()));
    }

    @Test
    void delete() {
        User user = new User(userId, "testUser", "email", "pass", Role.ROLE_USER);
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        mealRepository.save(meal, user.getId());
        Assertions.assertTrue(mealRepository.delete(meal.getId(), user.getId()));
        Assertions.assertNull(mealRepository.get(meal.getId(), user.getId()));
    }

    @Test
    void get() {
        User user = new User(userId, "testUser", "email", "pass", Role.ROLE_USER);
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        mealRepository.save(meal, user.getId());
        Assertions.assertEquals(meal, mealRepository.get(meal.getId(), user.getId()));
    }

    @Test
    void getAll() {
        User user = new User(userId, "testUser", "email", "pass", Role.ROLE_USER);
        Meal meal1 = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        Meal meal2 = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        mealRepository.save(meal1, user.getId());
        mealRepository.save(meal2, user.getId());
        List<Meal> list = mealRepository.getAll(user.getId());
        Assertions.assertEquals(List.of(meal1, meal2), list);
    }

    @ParameterizedTest
    @MethodSource("provideParametersForGetBetweenTest")
    void getBetween(LocalDateTime start, LocalDateTime end, int expectedSize) {
        System.out.println("USER ID " + userId);
        User user = new User(userId, "testUser", "email", "pass", Role.ROLE_USER);
        TestData.getMealList().forEach(meal -> mealRepository.save(meal, user.getId()));
        List<Meal> list = mealRepository.getBetween(start, end, user.getId());
        Assertions.assertEquals(expectedSize, list.size());
    }

    private static Stream<Arguments> provideParametersForGetBetweenTest() {
        return Stream.of(
                Arguments.of(LocalDateTime.of(2015, Month.MAY, 12, 0, 0),
                        LocalDateTime.of(2015, Month.MAY, 12, 12, 0), 2),
                Arguments.of(LocalDateTime.of(2015, Month.MAY, 12, 0, 0),
                        LocalDateTime.of(2015, Month.MAY, 12, 12, 0), 2)
        );
    }

    /*MEAL TEST DATA*/
    private static class TestData {
        public static List<Meal> getMealList() {
            Meal meal1 = new Meal(LocalDateTime.of(2015, Month.MAY, 12, 0, 0), "Завтрак", 1000);
            Meal meal2 = new Meal(LocalDateTime.of(2015, Month.MAY, 12, 12, 0), "Обед", 510);
            return Arrays.asList(meal1, meal2);
        }
    }
}