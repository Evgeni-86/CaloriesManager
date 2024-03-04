package ru.caloriesmanager.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.caloriesmanager.entity.Meal;
import ru.caloriesmanager.model.MealViewModel;
import ru.caloriesmanager.model.MealWithExcessModel;
import ru.caloriesmanager.service.MealService;
import ru.caloriesmanager.util.MealsUtil;
import ru.caloriesmanager.web.SecurityUtil;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class MealRestController {
    @Autowired
    private MealService mealService;

    @PostMapping("/meals")
    public MealViewModel create(@RequestBody MealViewModel mealViewModel) {
        Meal mealInstance = MealViewModel.getMealInstance(mealViewModel);
        mealService.create(mealInstance, SecurityUtil.authUserId());
        return MealViewModel.getModel(mealInstance);
    }

    @PutMapping("/meals")
    public String update(@RequestBody MealViewModel mealViewModel) {
        Meal meal = MealViewModel.getMealInstance(mealViewModel);
        mealService.update(meal, SecurityUtil.authUserId());
        return String.format("Meal with id %s was update", mealViewModel.getId());
    }

    @GetMapping("/meals/{id}")
    public MealViewModel get(@PathVariable(name = "id") int id) {
        return MealViewModel.getModel(mealService.get(id, SecurityUtil.authUserId()));
    }

    @DeleteMapping("/meals/{id}")
    public String delete(@PathVariable(name = "id") int id) {
        mealService.delete(id, SecurityUtil.authUserId());
        return String.format("Meal with id %s was deleted", id);
    }

    @GetMapping("/meals")
    public List<MealWithExcessModel> getAll() {
        return MealsUtil.getFilteredTos(mealService.getAll(SecurityUtil.authUserId()),
                SecurityUtil.authUserCaloriesPerDay(), LocalTime.MIN, LocalTime.MAX);
    }

    @PostMapping("/meals/filter")
    public List<MealWithExcessModel> getBetween(@RequestBody Map<String, String> dateTimes) {
        LocalDate startDate = LocalDate.parse(dateTimes.get("startDate"));
        LocalDate endDate = LocalDate.parse(dateTimes.get("endDate"));
        LocalTime startTime = LocalTime.parse(dateTimes.get("startTime"));
        LocalTime endTime = LocalTime.parse(dateTimes.get("endTime"));

        int userId = SecurityUtil.authUserId();
        List<Meal> mealsDateFiltered = mealService.getBetweenDates(startDate, endDate, userId);
        return MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }
}