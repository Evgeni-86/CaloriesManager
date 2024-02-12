package ru.caloriesmanager.transferObject;

import lombok.Getter;
import ru.caloriesmanager.model.AbstractBaseEntity;
import ru.caloriesmanager.model.Meal;

import java.time.LocalDateTime;

@Getter
public class UserMealWithExcess extends AbstractBaseEntity {

    private final LocalDateTime dateTime;
    private final String description;
    private final int calories;
    private final boolean excess;

    private UserMealWithExcess(Integer id, LocalDateTime dateTime, String description, int calories, boolean excess) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    public static UserMealWithExcess getTransferObject(Meal meal, boolean excess) {
        return new UserMealWithExcess(
                meal.getId(),
                meal.getDateTime(),
                meal.getDescription(),
                meal.getCalories(),
                excess);
    }

    @Override
    public String toString() {
        return "UserMealWithExcess{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }
}
