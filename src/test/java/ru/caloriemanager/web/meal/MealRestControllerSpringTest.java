package ru.caloriemanager.web.meal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.caloriemanager.model.*;
import ru.caloriemanager.util.exception.NotFoundException;
import ru.caloriemanager.web.SecurityUtil;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static java.util.Arrays.stream;


@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:spring/spring-app.xml")
class MealRestControllerSpringTest {

    private static MealRestController SUT;
    private static User user;

    @BeforeAll
    static void init(@Autowired MealRestController controller, @Autowired DataSource dataSource) {
        SUT = controller;
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScripts(new ClassPathResource("db/clearAndAddTestData.sql"));
        populator.execute(dataSource);
    }

    @BeforeEach
    void createUser() {
        SecurityUtil.setUserId(1);
        user = new User(1, null, null, null, Role.ROLE_USER);
    }

    @Test
    void create() {
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        meal.setUser(user);
        Assertions.assertEquals(meal, SUT.create(meal));
        Assertions.assertEquals(meal, SUT.get(meal.getId()));
    }

    @Test
    void createExceptionTest() {
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        meal.setUser(user);
        Assertions.assertEquals(meal, SUT.create(meal));
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> SUT.create(meal));
        Assertions.assertEquals(IllegalArgumentException.class, exception.getClass());
        Assertions.assertEquals(meal + " must be new (id=null)", exception.getMessage());
    }

    @Test
    void update() {
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        Meal mealUpdate = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "ЗавтракUpdate", 500);
        meal.setUser(user);
        mealUpdate.setUser(user);
        Assertions.assertEquals(meal, SUT.create(meal));
        mealUpdate.setId(meal.getId());
        SUT.update(mealUpdate);
        Assertions.assertEquals(mealUpdate, SUT.get(mealUpdate.getId()));
    }

    @Test
    void get() {
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        meal.setUser(user);
        Assertions.assertEquals(meal, SUT.create(meal));
        Assertions.assertEquals(meal, SUT.get(meal.getId()));
    }

    @Test
    void getExceptionTest() {
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        meal.setId(Integer.MAX_VALUE);
        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> SUT.get(Integer.MAX_VALUE));
        Assertions.assertEquals(NotFoundException.class, exception.getClass());
        Assertions.assertEquals("Not found entity with id=" + meal.getId(), exception.getMessage());
    }

    @Test
    void delete() {
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        meal.setUser(user);
        Assertions.assertEquals(meal, SUT.create(meal));
        Assertions.assertTrue(SUT.delete(meal.getId()));
        Assertions.assertThrows(NotFoundException.class, () -> SUT.get(meal.getId()));
    }

    @Test
    void deleteExceptionTest() {
        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> SUT.get(Integer.MAX_VALUE));
        Assertions.assertEquals(NotFoundException.class, exception.getClass());
        Assertions.assertEquals("Not found entity with id=" + Integer.MAX_VALUE, exception.getMessage());
    }

    @Test
    void getAll() {
        //Arrange
        int tempUserId = user.getId();
        SecurityUtil.setUserId(2);
        User user = new User(2, null, null, null, Role.ROLE_USER);
        Meal meal1 = new Meal(LocalDateTime.of(2017, Month.MAY,
                12, 0, 0), "Завтрак", 500);
        Meal meal2 = new Meal(LocalDateTime.of(2017, Month.MAY,
                12, 1, 0), "Обед", 500);
        Meal meal3 = new Meal(LocalDateTime.of(2017, Month.MAY,
                12, 2, 0), "Обед", 500);
        meal1.setUser(user);
        meal2.setUser(user);
        meal3.setUser(user);
        SUT.create(meal1);
        SUT.create(meal2);
        SUT.create(meal3);
        //Act
        List<MealTo> mealList = SUT.getAll();
        SecurityUtil.setUserId(tempUserId);
        //Assert
        assertThat(mealList).usingRecursiveComparison()
                .ignoringFields("excess").isEqualTo(List.of(meal3, meal2, meal1));
    }

    @Test
    void getBetween() {
//        Meal meal1 = new Meal(LocalDateTime.of(2017, Month.MAY,
//                12, 0, 0), "Завтрак", 500);
//        Meal meal2 = new Meal(LocalDateTime.of(2017, Month.MAY,
//                12, 12, 0), "Обед", 500);
//        mealRestController.create(meal1);
//        mealRestController.create(meal2);
//        List<MealTo> mealToList = mealRestController.getBetween(
//                LocalDate.of(2017, Month.MAY, 12),
//                LocalTime.of(0, 0),
//                LocalDate.of(2017, Month.MAY, 12),
//                LocalTime.of(12, 0));
//        Assertions.assertNotNull(mealToList);
//        Assertions.assertEquals(2, mealToList.size());
    }
}