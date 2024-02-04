package ru.caloriemanager.repository.orm;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.caloriemanager.model.Meal;
import ru.caloriemanager.model.Role;
import ru.caloriemanager.model.User;
import ru.caloriemanager.repository.MealRepository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:spring/spring-app.xml")
class OrmMealRepositoryTest {

    private static MealRepository SUT;
    private static int currentUserId = 0;
    private static User user;

    @BeforeAll
    static void init(@Autowired MealRepository ormMealRepository, @Autowired DataSource dataSource) {
        SUT = ormMealRepository;
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScripts(new ClassPathResource("db/clearAndAddTestData.sql"));
        populator.execute(dataSource);
    }

    @BeforeEach
    void userIdIncrement() {
        currentUserId++;
        user = new User(currentUserId, null, null, null, Role.ROLE_USER);
    }

    @Test
    void saveNew() {
        //Arrange
        Meal meal = new Meal(null, "meal test", 1000);
        meal.setUser(user);
        //Act
        Meal actual = SUT.save(meal, currentUserId);
        Meal checkWrite = SUT.get(meal.getId(), currentUserId);
        //Assert
        Assertions.assertFalse(meal.isNew());
        Assertions.assertEquals(meal, actual);
        Assertions.assertEquals(meal, checkWrite);
    }

    @Test
    void saveUpdate() {
        //Arrange
        Meal meal = new Meal(null, "meal test", 1000);
        meal.setUser(user);
        Meal mealUpdate = new Meal(null, "meal test update", 2000);
        mealUpdate.setUser(user);
        //Act
        SUT.save(meal, currentUserId);
        mealUpdate.setId(meal.getId());
        Meal actual = SUT.save(mealUpdate, currentUserId);
        //Assert
        Assertions.assertEquals(mealUpdate, actual);
        Assertions.assertEquals(mealUpdate, SUT.get(mealUpdate.getId(), currentUserId));
    }

    @Test
    void delete() {
        //Arrange
        Meal meal = new Meal(null, "meal test", 1000);
        meal.setUser(user);
        SUT.save(meal, currentUserId);
        //Act
        boolean result = SUT.delete(meal.getId(), currentUserId);
        //Assert
        assertTrue(result);
        Assertions.assertNull(SUT.get(meal.getId(), currentUserId));
    }

    @Test
    void get() {
        //Arrange
        Meal meal = new Meal(null, "meal test", 1000);
        meal.setUser(user);
        SUT.save(meal, currentUserId);
        //Act
        Meal result = SUT.get(meal.getId(), currentUserId);
        //Assert
        Assertions.assertEquals(meal, result);
    }

    @Test
    void getAll() {
        //Arrange
        Meal meal1 = new Meal(null, "meal1", 1000);
        Meal meal2 = new Meal(null, "meal2", 1000);
        Meal meal3 = new Meal(null, "meal3", 1000);
        meal1.setUser(user);
        meal2.setUser(user);
        meal3.setUser(user);
        List<Meal> mealList = Arrays.asList(meal1, meal2, meal3);
        mealList.forEach((el) -> SUT.save(el, currentUserId));
        Collections.reverse(mealList);
        //Act
        List<Meal> actual = SUT.getAll(currentUserId);
        //Assert
        Assertions.assertEquals(mealList, actual);
    }

    @Test
    void getBetween() {
        //Arrange
        LocalDateTime dateTime1 = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(23, 59));
        LocalDateTime dateTime2 = LocalDateTime.of(LocalDate.now().minusDays(2), LocalTime.MIN);
        LocalDateTime dateTime3 = LocalDateTime.of(LocalDate.now().minusDays(3), LocalTime.of(0, 1));
        Meal meal1 = new Meal(dateTime1,"test1", 1000);
        Meal meal2 = new Meal(dateTime2,"test2", 1000);
        Meal meal3 = new Meal(dateTime3,"test3", 1000);
        meal1.setUser(user);
        meal2.setUser(user);
        meal3.setUser(user);
        SUT.save(meal1, currentUserId);
        SUT.save(meal2, currentUserId);
        SUT.save(meal3, currentUserId);
        List<LocalDateTime> expectedDateTime = Arrays.asList(dateTime1, dateTime2, dateTime3);
        //Act
        List<Meal> result = SUT.getBetween(dateTime3, dateTime1, currentUserId);
        List<LocalDateTime> actual = result.stream().map(el -> el.getDateTime()).toList();
        //Assert
        Assertions.assertEquals(expectedDateTime, actual);
    }

}