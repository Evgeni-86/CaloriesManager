package ru.caloriemanager.web.meal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.caloriemanager.model.*;
import ru.caloriemanager.util.exception.NotFoundException;
import ru.caloriemanager.web.SecurityUtil;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

import static java.util.Arrays.stream;


@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:spring/spring-app.xml")
class MealRestControllerSpringTest {

    private static MealRestController mealRestController;

    @BeforeAll
    static void init(@Autowired MealRestController controller) {
        SecurityUtil.setUserId(10);
        mealRestController = controller;
    }

    @Test
    void create() {
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        Assertions.assertEquals(meal, mealRestController.create(meal));
        Assertions.assertEquals(meal, mealRestController.get(meal.getId()));
    }

    @Test
    void createExceptionTest() {
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        Assertions.assertEquals(meal, mealRestController.create(meal));
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> mealRestController.create(meal));
        Assertions.assertEquals(IllegalArgumentException.class, exception.getClass());
        Assertions.assertEquals(meal + " must be new (id=null)", exception.getMessage());
    }

    @Test
    void update() {
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        Meal mealUpdate = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "ЗавтракUpdate", 500);
        Assertions.assertEquals(meal, mealRestController.create(meal));
        mealUpdate.setId(meal.getId());
        mealRestController.update(mealUpdate);
        Assertions.assertEquals(mealUpdate, mealRestController.get(mealUpdate.getId()));
    }

    @Test
    void get() {
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        Assertions.assertEquals(meal, mealRestController.create(meal));
        Assertions.assertEquals(meal, mealRestController.get(meal.getId()));
    }

    @Test
    void getExceptionTest() {
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        meal.setId(Integer.MAX_VALUE);
        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> mealRestController.get(Integer.MAX_VALUE));
        Assertions.assertEquals(NotFoundException.class, exception.getClass());
        Assertions.assertEquals("Not found entity with id=" + meal.getId(), exception.getMessage());
    }

    @Test
    void delete() {
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        Assertions.assertEquals(meal, mealRestController.create(meal));
        Assertions.assertTrue(mealRestController.delete(meal.getId()));
        Assertions.assertThrows(NotFoundException.class, () -> mealRestController.get(meal.getId()));
    }

    @Test
    void deleteExceptionTest() {
        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> mealRestController.get(Integer.MAX_VALUE));
        Assertions.assertEquals(NotFoundException.class, exception.getClass());
        Assertions.assertEquals("Not found entity with id=" + Integer.MAX_VALUE, exception.getMessage());
    }

    @Test
    void getAll() {
        int tempUserId = SecurityUtil.authUserId();
        SecurityUtil.setUserId(100);
        Meal meal1 = new Meal(LocalDateTime.of(2017, Month.MAY,
                12, 0, 0), "Завтрак", 500);
        Meal meal2 = new Meal(LocalDateTime.of(2017, Month.MAY,
                12, 1, 0), "Обед", 500);
        Meal meal3 = new Meal(LocalDateTime.of(2017, Month.MAY,
                12, 2, 0), "Обед", 500);
        mealRestController.create(meal1);
        mealRestController.create(meal2);
        mealRestController.create(meal3);
        List<MealTo> mealList = mealRestController.getAll();
        SecurityUtil.setUserId(tempUserId);
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