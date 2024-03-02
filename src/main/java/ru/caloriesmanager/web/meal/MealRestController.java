package ru.caloriesmanager.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.caloriesmanager.entity.Meal;
import ru.caloriesmanager.model.MealWithExcessModel;
import ru.caloriesmanager.service.MealService;
import ru.caloriesmanager.util.MealsUtil;
import ru.caloriesmanager.web.SecurityUtil;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@RestController
@RequestMapping("/api")
public class MealRestController {
    @Autowired
    private MealService mealService;

    public Meal create(Meal meal) {
        return mealService.create(meal, SecurityUtil.authUserId());
    }

    public void update(Meal meal) {
        mealService.update(meal, SecurityUtil.authUserId());
    }

    @GetMapping("/meal")
    public Meal get(int id) {
        return mealService.get(id, SecurityUtil.authUserId());
    }

    public boolean delete(int id) {
        return mealService.delete(id, SecurityUtil.authUserId());
    }

    public List<MealWithExcessModel> getAll() {
        return MealsUtil.getFilteredTos(mealService.getAll(SecurityUtil.authUserId()),
                SecurityUtil.authUserCaloriesPerDay(), LocalTime.MIN, LocalTime.MAX);
    }

    public List<MealWithExcessModel> getBetween(@Nullable LocalDate startDate, @Nullable LocalTime startTime,
                                                @Nullable LocalDate endDate, @Nullable LocalTime endTime) {
        int userId = SecurityUtil.authUserId();
        List<Meal> mealsDateFiltered = mealService.getBetweenDates(startDate, endDate, userId);
        return MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }
}