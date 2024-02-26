package ru.caloriesmanager.model;

import lombok.Getter;
import lombok.Setter;
import ru.caloriesmanager.entity.Meal;
import ru.caloriesmanager.util.DateTimeUtil;

import java.time.LocalDateTime;


@Getter
@Setter
public class MealViewModel {

    private Integer id;
    private String formattedDateTime;
    private LocalDateTime dateTime;
    private String description;
    private int calories;

    public MealViewModel() {
    }

    public MealViewModel(Integer id, LocalDateTime dateTime, String description, int calories) {
        this.id = id;
        this.dateTime = dateTime;
        this.formattedDateTime = DateTimeUtil.toString(dateTime);
        this.description = description;
        this.calories = calories;
    }

    public static MealViewModel getModel(Meal meal) {
        return new MealViewModel(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
    }
}
