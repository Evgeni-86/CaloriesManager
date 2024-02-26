package ru.caloriesmanager.web.meal;

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
import ru.caloriesmanager.entity.*;
import ru.caloriesmanager.model.MealWithExcessModel;
import ru.caloriesmanager.util.exception.NotFoundException;
import ru.caloriesmanager.web.SecurityUtil;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

import static java.util.Arrays.stream;


@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:spring/spring-app.xml")
class MealRestControllerTest {

    private static MealRestController SUT;
    private static User user;

    @BeforeAll
    static void init(@Autowired MealRestController controller, @Autowired DataSource dataSource) {
        SUT = controller;
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScripts(new ClassPathResource("db/clearUsersMealsRolesAndAddTestData.sql"));
        populator.execute(dataSource);
    }

    @BeforeEach
    void initUser() {
        SecurityUtil.setUserId(1);
        user = new User(1, null, null, null, Role.ROLE_USER);
    }

    @Test
    void create() {
        //Arrange
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        //Act
        //Assert
        Assertions.assertEquals(meal, SUT.create(meal));
        Assertions.assertEquals(meal, SUT.get(meal.getId()));
    }

    @Test
    void createExceptionTest() {
        //Arrange
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        SUT.create(meal);
        //Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> SUT.create(meal));
        //Assert
        Assertions.assertEquals(IllegalArgumentException.class, exception.getClass());
        Assertions.assertEquals(meal + " must be new (id=null)", exception.getMessage());
    }

    @Test
    void update() {
        //Arrange
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        SUT.create(meal);
        meal.setDescription("update");
        meal.setCalories(100);
        //Act
        SUT.update(meal);
        //Assert
        Assertions.assertEquals(meal, SUT.get(meal.getId()));
    }

    @Test
    void get() {
        //Arrange
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        //Act
        //Assert
        Assertions.assertEquals(meal, SUT.create(meal));
        Assertions.assertEquals(meal, SUT.get(meal.getId()));
    }

    @Test
    void getExceptionTest() {
        //Arrange
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        meal.setId(Integer.MAX_VALUE);
        //Act
        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> SUT.get(Integer.MAX_VALUE));
        //Assert
        Assertions.assertEquals(NotFoundException.class, exception.getClass());
        Assertions.assertEquals("Not found entity with id=" + meal.getId(), exception.getMessage());
    }

    @Test
    void delete() {
        //Arrange
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        SUT.create(meal);
        //Act
        //Assert
        Assertions.assertTrue(SUT.delete(meal.getId()));
        Assertions.assertThrows(NotFoundException.class, () -> SUT.get(meal.getId()));
    }

    @Test
    void deleteExceptionTest() {
        //Arrange
        //Act
        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> SUT.delete(Integer.MAX_VALUE));
        //Assert
        Assertions.assertEquals("Not found entity with id=" + Integer.MAX_VALUE, exception.getMessage());
    }

    @Test
    void getAll() {
        //Arrange
        SecurityUtil.setUserId(2);
        User user = new User(2, null, null, null, Role.ROLE_USER);
        Meal meal1 = new Meal(LocalDateTime.of(2017, Month.MAY,
                12, 0, 0), "Завтрак", 500);
        Meal meal2 = new Meal(LocalDateTime.of(2017, Month.MAY,
                12, 1, 0), "Обед", 500);
        Meal meal3 = new Meal(LocalDateTime.of(2017, Month.MAY,
                12, 2, 0), "Обед", 500);
        SUT.create(meal1);
        SUT.create(meal2);
        SUT.create(meal3);
        //Act
        List<MealWithExcessModel> actual = SUT.getAll();
        //Assert
        Assertions.assertNotNull(actual);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("excess").isEqualTo(List.of(meal3, meal2, meal1));
    }

    @Test
    void getBetween() {
        //Arrange
        SecurityUtil.setUserId(3);
        User user = new User(3, null, null, null, Role.ROLE_USER);
        Meal meal1 = new Meal(LocalDateTime.of(2017, Month.MAY,
                12, 0, 0), "Завтрак", 500);
        Meal meal2 = new Meal(LocalDateTime.of(2017, Month.MAY,
                12, 12, 0), "Обед", 500);
        SUT.create(meal1);
        SUT.create(meal2);
        //Act
        List<MealWithExcessModel> actual = SUT.getBetween(
                LocalDate.of(2017, Month.MAY, 12),
                LocalTime.of(0, 0),
                LocalDate.of(2017, Month.MAY, 12),
                LocalTime.of(12, 0));
        //Assert
        Assertions.assertNotNull(actual);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("excess").isEqualTo(List.of(meal2, meal1));
    }
}