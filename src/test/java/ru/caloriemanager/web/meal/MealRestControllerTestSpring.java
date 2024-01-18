package ru.caloriemanager.web.meal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;
import ru.caloriemanager.model.*;
import ru.caloriemanager.service.MealService;
import ru.caloriemanager.util.exception.NotFoundException;
import ru.caloriemanager.web.MealTestData;
import ru.caloriemanager.web.SecurityUtil;

import javax.swing.undo.CannotUndoException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.util.Arrays.stream;


@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:spring/spring-app.xml")
class MealRestControllerTestSpring {

    private static MealRestController mealRestController;


    @BeforeAll
    static void init(@Autowired MealRestController controller) {
        SecurityUtil.setUserId(10);
        mealRestController = controller;
        MealTestData.USERS_MEALS.keySet().forEach(meal -> mealRestController.create(meal));
    }

    @Test
    void create() {
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        Assertions.assertEquals(meal, mealRestController.create(meal));
    }

    @Test
    void createExceptionTest() {
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
//        Meal meal2 = new Meal(LocalDateTime.of(2015, Month.MAY,
//                30, 13, 0), "Завтрак", 500);
        Assertions.assertEquals(meal, mealRestController.create(meal));
        meal.setId(1);
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
        Meal meal1 = new Meal(LocalDateTime.of(2016, Month.MAY,
                12, 0, 0), "Завтрак", 500);
        Meal meal2 = new Meal(LocalDateTime.of(2016, Month.MAY,
                12, 12, 0), "Обед", 500);
        mealRestController.create(meal1);
        mealRestController.create(meal2);
        List<MealTo> mealList = mealRestController.getAll();
        SecurityUtil.setUserId(tempUserId);
        Assertions.assertNotNull(mealList);
        Assertions.assertEquals(2, mealList.size());
    }

    @Test
    void getBetween() {
        Meal meal1 = new Meal(LocalDateTime.of(2017, Month.MAY,
                12, 0, 0), "Завтрак", 500);
        Meal meal2 = new Meal(LocalDateTime.of(2017, Month.MAY,
                12, 12, 0), "Обед", 500);
        mealRestController.create(meal1);
        mealRestController.create(meal2);
        List<MealTo> mealToList = mealRestController.getBetween(
                LocalDate.of(2017, Month.MAY, 12),
                LocalTime.of(0, 0),
                LocalDate.of(2017, Month.MAY, 12),
                LocalTime.of(12, 0));
        Assertions.assertNotNull(mealToList);
        Assertions.assertEquals(2, mealToList.size());
    }
}