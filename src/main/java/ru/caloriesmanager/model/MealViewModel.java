package ru.caloriesmanager.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.caloriesmanager.entity.Meal;
import ru.caloriesmanager.util.DateTimeUtil;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class MealViewModel {

    private Integer id;
    private String formattedDateTime;
    @NotNull(message = "{NotEmpty.MealViewModel.dateTime}")
    private LocalDateTime dateTime;
    @NotEmpty(message = "{NotEmpty.MealViewModel.description}")
    private String description;
    @Min(value = 1, message = "Min.MealViewModel.calories")
    private int calories;

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
}
