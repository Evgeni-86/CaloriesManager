package ru.caloriesmanager.model;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.caloriesmanager.entity.Meal;
import ru.caloriesmanager.util.DateTimeUtil;
import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
@Setter
public class MealViewModel {

    private Integer id;
    private String formattedDateTime;
    @NotNull(message = "{NotNull.MealViewModel.dateTime}")
    private LocalDateTime dateTime;
    @NotBlank(message = "{NotBlank.MealViewModel.description}")
    private String description;
    @NotNull(message = "{NotNull.MealViewModel.calories}")
    private Integer calories;

    public MealViewModel(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public MealViewModel(Integer id, LocalDateTime dateTime, String description, int calories) {
        this.id = id;
        this.dateTime = dateTime;
        this.formattedDateTime = DateTimeUtil.toString(dateTime);
        this.description = description;
        this.calories = calories;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        this.formattedDateTime = DateTimeUtil.toString(dateTime);
    }

    public static MealViewModel getModel(Meal meal) {
        return new MealViewModel(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
    }

    public static Meal getMealInstance(MealViewModel mealViewModel) {
        return new Meal(
                mealViewModel.getId() == null ? null : mealViewModel.getId(),
                mealViewModel.getDateTime(),
                mealViewModel.getDescription(),
                mealViewModel.getCalories());
    }

    @Override
    public String toString() {
        return "MealViewModel{" +
                "id=" + id +
                ", formattedDateTime='" + formattedDateTime + '\'' +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
