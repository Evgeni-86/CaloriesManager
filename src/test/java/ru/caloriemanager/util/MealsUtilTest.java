package ru.caloriemanager.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.caloriemanager.model.AbstractBaseEntity;
import ru.caloriemanager.model.Meal;
import ru.caloriemanager.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MealsUtilTest {
    //Arrange
    //Act
    //Assert

    @Test
    void getFilteredTos() {
        //Arrange
        List<Meal> testList = Arrays.asList(
                new Meal(1, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(2, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(3, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(4, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(5, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(6, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(13, 0);
        List<Integer> idExpectedMeals = Arrays.asList(1, 2, 4, 5);
        List<Integer> idExpectedExcessesMeals = Arrays.asList(4, 5);
        //Act
        List<MealTo> result = MealsUtil.getFilteredTos(testList, 2000, startTime, endTime);
        List<Integer> resultExpected = result.stream().map(el -> el.getId()).toList();
        List<MealTo> listExcess = result.stream().filter(MealTo::isExcess).toList();
        List<Integer> resultExcessExpected = listExcess.stream().map(el -> el.getId()).toList();
        //Assert
        Assertions.assertEquals(idExpectedMeals, resultExpected);
        Assertions.assertEquals(idExpectedExcessesMeals, resultExcessExpected);
    }
}