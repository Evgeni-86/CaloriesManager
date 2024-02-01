package ru.caloriemanager.repository.orm;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.caloriemanager.model.Meal;
import ru.caloriemanager.model.Role;
import ru.caloriemanager.model.User;
import ru.caloriemanager.repository.MealRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:spring/spring-app.xml")
class OrmMealRepositoryTest {

    private static MealRepository SUT;
    private static int CURRENT_USER_ID = 0;

    @BeforeAll
    static void init(@Autowired MealRepository ormMealRepository) {
        SUT = ormMealRepository;
    }

    @BeforeEach
    void userIdIncrement() {
        CURRENT_USER_ID++;
    }

    @Test
    void saveNew() {
        //Arrange
        Meal meal = new Meal(null, "meal test", 1000);
        User user = new User(1, null, null, null, Role.ROLE_USER);
        meal.setUser(user);
        //Act
        Meal actual = SUT.save(meal, CURRENT_USER_ID);
        Meal checkWrite = SUT.get(meal.getId(), CURRENT_USER_ID);
        //Assert
        Assertions.assertFalse(meal.isNew());
        Assertions.assertEquals(meal, actual);
        Assertions.assertEquals(meal, checkWrite);
    }

    @Disabled
    @Test
    void saveUpdate() {
        //Arrange
        Meal meal = new Meal(null, "meal test", 1000);
        Meal mealUpdate = new Meal(null, "meal test update", 2000);
        //Act
        SUT.save(meal, CURRENT_USER_ID);
        mealUpdate.setId(meal.getId());
        Meal result = SUT.save(mealUpdate, CURRENT_USER_ID);
        //Assert
        Assertions.assertEquals(mealUpdate, result);
        Assertions.assertEquals(mealUpdate, SUT.get(mealUpdate.getId(), CURRENT_USER_ID));
    }

    @Disabled
    @Test
    void delete() {
        //Arrange
        Meal meal = new Meal(null, "meal test", 1000);
        SUT.save(meal, CURRENT_USER_ID);
        //Act
        boolean result = SUT.delete(meal.getId(), CURRENT_USER_ID);
        //Assert
        assertTrue(result);
        Assertions.assertNull(SUT.get(meal.getId(), CURRENT_USER_ID));
    }

    @Disabled
    @Test
    void get() {
        //Arrange
        Meal meal = new Meal(null, "meal test with id", 1000);
        SUT.save(meal, CURRENT_USER_ID);
        //Act
        Meal result = SUT.get(meal.getId(), CURRENT_USER_ID);
        //Assert
        Assertions.assertEquals(meal, result);
    }

    @Disabled
    @Test
    void getAll() {
        //Arrange
        Meal meal1 = new Meal(null, "meal test with id", 1000);
        Meal meal2 = new Meal(null, "meal test with id", 1000);
        Meal meal3 = new Meal(null, "meal test with id", 1000);
        List<Meal> mealList = Arrays.asList(meal1, meal3, meal2);
        mealList.forEach((el) -> SUT.save(el, CURRENT_USER_ID));
        //Act
        List<Meal> result = SUT.getAll(CURRENT_USER_ID);
        //Assert
        Assertions.assertEquals(mealList, result);
    }

    @Disabled
    @Test
    void getBetween() {
//        //Arrange
//        String sql = "INSERT INTO meals (date_time, description, calories, user_id) VALUES(?, ?, ?, ?)";
//        LocalDateTime dateTime1 = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(23, 59));
//        LocalDateTime dateTime2 = LocalDateTime.of(LocalDate.now().minusDays(2), LocalTime.MIN);
//        LocalDateTime dateTime3 = LocalDateTime.of(LocalDate.now().minusDays(3), LocalTime.of(0, 1));
//        jdbcTemplate.update(sql, dateTime1, "meal test", 1000, CURRENT_USER_ID);
//        jdbcTemplate.update(sql, dateTime2, "meal test", 1000, CURRENT_USER_ID);
//        jdbcTemplate.update(sql, dateTime3, "meal test", 1000, CURRENT_USER_ID);
//        List<LocalDateTime> expectedDateTime = Arrays.asList(dateTime1, dateTime2, dateTime3);
//        //Act
//        List<Meal> result = SUT.getBetween(dateTime3, dateTime1, CURRENT_USER_ID);
//        List<LocalDateTime> resultDates = result.stream().map(el -> el.getDateTime()).toList();
//        //Assert
//        Assertions.assertEquals(expectedDateTime, resultDates);
    }

}