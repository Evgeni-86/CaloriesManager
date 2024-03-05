package ru.caloriesmanager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.caloriesmanager.entity.Meal;
import ru.caloriesmanager.entity.Role;
import ru.caloriesmanager.entity.User;
import ru.caloriesmanager.service.MealService;
import ru.caloriesmanager.service.UserService;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Set;


@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:spring/spring-app.xml")
public class CacheTest {

    private static UserService SUT_user;
    private static MealService SUT_meal;

    @BeforeAll
    static void init(@Autowired MealService mealService,
                     @Autowired UserService userService,
                     @Autowired DataSource dataSource) {
        SUT_meal = mealService;
        SUT_user = userService;
    }

    @Sql("classpath:db/clearUsersMealsRolesTables.sql")
    @Test
    void getALL() {
        //Arrange
        User user1 = new User(null, "new user1", "user1@email",
                "pass", 2000, true, Set.of(Role.ROLE_USER));
        User user2 = new User(null, "new user2", "user2@email",
                "pass", 2000, true, Set.of(Role.ROLE_USER));
        User user3 = new User(null, "new user3", "user3@email",
                "pass", 2000, true, Set.of(Role.ROLE_USER));
        SUT_user.create(user1);
        SUT_user.create(user2);
        SUT_user.create(user3);
        //Act
        long start = System.currentTimeMillis();
        List<User> actual1 = SUT_user.getAll();
        long medium = System.currentTimeMillis();
        List<User> actual2 = SUT_user.getAll();
        long end = System.currentTimeMillis();
        //Assert
        System.out.printf("No cache ms: %s, yes cache ms: %s\n", (medium - start), (end - medium));
        Assertions.assertTrue(actual1 == actual2);
    }

    @Sql("classpath:db/clearUsersMealsRolesAndAddTestData.sql")
    @Test
    void getAllMealCache() {
        //Arrange
        int userId = 1;
        Meal meal1 = new Meal(LocalDateTime.of(2017, Month.MAY,
                12, 0, 0), "Завтрак", 500);
        Meal meal2 = new Meal(LocalDateTime.of(2017, Month.MAY,
                12, 1, 0), "Обед", 500);
        Meal meal3 = new Meal(LocalDateTime.of(2017, Month.MAY,
                12, 2, 0), "Обед", 500);
        SUT_meal.create(meal1, userId);
        SUT_meal.create(meal2, userId);
        SUT_meal.create(meal3, userId);
        //Act
        long start = System.currentTimeMillis();
        List<Meal> actual1 = SUT_meal.getAll(userId);
        long medium = System.currentTimeMillis();
        List<Meal> actual2 = SUT_meal.getAll(userId);
        long end = System.currentTimeMillis();
        //Assert
        System.out.printf("No cache ms: %s, yes cache ms: %s\n", (medium - start), (end - medium));
        Assertions.assertTrue(actual1 == actual2);
    }
}
