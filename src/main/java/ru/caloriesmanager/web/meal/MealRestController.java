package ru.caloriesmanager.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.caloriesmanager.entity.Meal;
import ru.caloriesmanager.model.MealViewModel;
import ru.caloriesmanager.model.MealWithExcessModel;
import ru.caloriesmanager.service.CustomUserDetailsService;
import ru.caloriesmanager.service.MealService;
import ru.caloriesmanager.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class MealRestController {
    @Autowired
    private MealService mealService;

    @PostMapping("/meals")
    public MealViewModel create(@RequestBody MealViewModel mealViewModel) {
        int userId = CustomUserDetailsService.getCustomUserDetails().getUser().getId();
        Meal mealInstance = MealViewModel.getMealInstance(mealViewModel);
        mealService.create(mealInstance, userId);
        return MealViewModel.getModel(mealInstance);
    }

    @PutMapping("/meals")
    public String update(@RequestBody MealViewModel mealViewModel) {
        int userId = CustomUserDetailsService.getCustomUserDetails().getUser().getId();
        Meal meal = MealViewModel.getMealInstance(mealViewModel);
        mealService.update(meal, userId);
        return String.format("Meal with id %s was update", mealViewModel.getId());
    }

    @GetMapping("/meals/{id}")
    public MealViewModel get(@PathVariable(name = "id") int id) {
        int userId = CustomUserDetailsService.getCustomUserDetails().getUser().getId();
        return MealViewModel.getModel(mealService.get(id, userId));
    }

    @DeleteMapping("/meals/{id}")
    public String delete(@PathVariable(name = "id") int id) {
        int userId = CustomUserDetailsService.getCustomUserDetails().getUser().getId();
        mealService.delete(id, userId);
        return String.format("Meal with id %s was deleted", id);
    }

    @GetMapping("/meals")
    public List<MealWithExcessModel> getAll() {
        int userId = CustomUserDetailsService.getCustomUserDetails().getUser().getId();
        int userCaloriesPerDay = CustomUserDetailsService.getCustomUserDetails().getUser().getCaloriesPerDay();
        return MealsUtil.getFilteredTos(mealService.getAll(userId), userCaloriesPerDay, LocalTime.MIN, LocalTime.MAX);
    }

    @PostMapping("/meals/filter")
    public List<MealWithExcessModel> getBetween(@RequestBody Map<String, String> dateTimes) {
        LocalDate startDate = LocalDate.parse(dateTimes.get("startDate"));
        LocalDate endDate = LocalDate.parse(dateTimes.get("endDate"));
        LocalTime startTime = LocalTime.parse(dateTimes.get("startTime"));
        LocalTime endTime = LocalTime.parse(dateTimes.get("endTime"));

        int userCaloriesPerDay = CustomUserDetailsService.getCustomUserDetails().getUser().getCaloriesPerDay();
        int userId = CustomUserDetailsService.getCustomUserDetails().getUser().getId();
        ZoneId zoneId = CustomUserDetailsService.getCustomUserDetails().getZoneId();
        List<Meal> mealsDateFiltered = mealService.getBetweenDates(startDate, endDate, zoneId, userId);
        return MealsUtil.getFilteredTos(mealsDateFiltered, userCaloriesPerDay, startTime, endTime);
    }
}