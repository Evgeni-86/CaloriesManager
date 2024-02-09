package ru.caloriesmanager.util;

import ru.caloriesmanager.model.Meal;
import ru.caloriesmanager.transferObject.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class MealsUtil {
    public static List<UserMealWithExcess> getFilteredTos(Collection<Meal> meals, int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        return getFiltered(meals, caloriesPerDay, meal -> DateTimeUtil.isBetweenInclusive(meal.getTime(), startTime, endTime));
    }

    private static List<UserMealWithExcess> getFiltered(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories)));
        return meals.stream()
                .filter(filter)
                .map(meal -> UserMealWithExcess.getModel(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(toList());
    }

//    private static UserMealWithExcess createTo(Meal meal, boolean excess) {
//        return new UserMealWithExcess(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
//    }
}