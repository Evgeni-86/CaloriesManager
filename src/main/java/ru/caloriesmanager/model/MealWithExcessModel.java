package ru.caloriesmanager.model;

import lombok.Getter;
import ru.caloriesmanager.entity.AbstractBaseEntity;
import ru.caloriesmanager.entity.Meal;

import java.time.LocalDateTime;

@Getter
public class MealWithExcessModel {

    private final Integer id;
    private final LocalDateTime dateTime;
    private final String description;
    private final int calories;
    private final boolean excess;

    private MealWithExcessModel(Integer id, LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    public static MealWithExcessModel getModel(Meal meal, boolean excess) {
        return new MealWithExcessModel(
                meal.getId(),
                meal.getDateTime(),
                meal.getDescription(),
                meal.getCalories(),
                excess);
    }

    @Override
    public String toString() {
        return "MealWithExcessModel{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }
}
