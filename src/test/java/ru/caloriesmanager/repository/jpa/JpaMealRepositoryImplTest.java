package ru.caloriesmanager.repository.jpa;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.caloriesmanager.entity.Meal;
import ru.caloriesmanager.entity.Role;
import ru.caloriesmanager.entity.User;
import ru.caloriesmanager.repository.MealRepository;

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
class JpaMealRepositoryImplTest {

    private static MealRepository SUT;
    private static int currentUserId;
    private static User user;

    @BeforeAll
    static void init(@Autowired MealRepository ormMealRepository, @Autowired DataSource dataSource) {
        SUT = ormMealRepository;
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScripts(new ClassPathResource("db/clearUsersMealsRolesAndAddTestData.sql"));
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
        //Act
        Meal actual = SUT.save(meal, user.getId());
        Meal checkWrite = SUT.get(meal.getId(), user.getId());
        //Assert
        Assertions.assertFalse(meal.isNew());
        Assertions.assertEquals(meal, actual);
        Assertions.assertEquals(meal, checkWrite);
    }

    @Test
    void saveUpdate() {
        //Arrange
        Meal meal = new Meal(null, "meal test", 1000);
        SUT.save(meal, user.getId());
        meal.setDescription("meal test update");
        meal.setCalories(1500);
        //Act
        Meal actual = SUT.save(meal, user.getId());
        //Assert
        Assertions.assertEquals(meal, actual);
        Assertions.assertEquals(meal, SUT.get(meal.getId(), user.getId()));
    }

    @Test
    void delete() {
        //Arrange
        Meal meal = new Meal(null, "meal test", 1000);
        SUT.save(meal, user.getId());
        //Act
        boolean actual = SUT.delete(meal.getId(), user.getId());
        //Assert
        assertTrue(actual);
        Assertions.assertNull(SUT.get(meal.getId(), user.getId()));
    }

    @Test
    void get() {
        //Arrange
        Meal meal = new Meal(null, "meal test", 1000);
        SUT.save(meal, user.getId());
        //Act
        Meal actual = SUT.get(meal.getId(), user.getId());
        //Assert
        Assertions.assertEquals(meal, actual);
    }

    @Test
    void getAll() {
        //Arrange
        Meal meal1 = new Meal(null, "meal1", 1000);
        Meal meal2 = new Meal(null, "meal2", 1000);
        Meal meal3 = new Meal(null, "meal3", 1000);
        List<Meal> mealList = Arrays.asList(meal1, meal2, meal3);
        mealList.forEach((el) -> SUT.save(el, user.getId()));
        Collections.reverse(mealList);
        //Act
        List<Meal> actual = SUT.getAll(user.getId());
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
        SUT.save(meal1, user.getId());
        SUT.save(meal2, user.getId());
        SUT.save(meal3, user.getId());
        List<LocalDateTime> expectedDateTime = Arrays.asList(dateTime1, dateTime2, dateTime3);
        //Act
        List<Meal> result = SUT.getBetween(dateTime3, dateTime1, user.getId());
        List<LocalDateTime> actual = result.stream().map(el -> el.getDateTime()).toList();
        //Assert
        Assertions.assertEquals(expectedDateTime, actual);
    }

}