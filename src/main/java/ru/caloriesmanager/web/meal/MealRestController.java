package ru.caloriesmanager.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import ru.caloriesmanager.model.Meal;
import ru.caloriesmanager.transferObject.UserMealWithExcess;
import ru.caloriesmanager.service.MealService;
import ru.caloriesmanager.util.MealsUtil;
import ru.caloriesmanager.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {
    private static final Logger LOG = LoggerFactory.getLogger(MealRestController.class);
    @Autowired
    private MealService mealService;

    public Meal create(Meal meal) {
        return mealService.create(meal, SecurityUtil.authUserId());
    }

    public void update(Meal meal) {
        mealService.update(meal, SecurityUtil.authUserId());
    }

    public Meal get(int id) {
        return mealService.get(id, SecurityUtil.authUserId());
    }

    public boolean delete(int id) {
        return mealService.delete(id, SecurityUtil.authUserId());
    }

    public List<UserMealWithExcess> getAll() {
        return MealsUtil.getFilteredTos(mealService.getAll(SecurityUtil.authUserId()),
                SecurityUtil.authUserCaloriesPerDay(), LocalTime.MIN, LocalTime.MAX);
    }

    public List<UserMealWithExcess> getBetween(@Nullable LocalDate startDate, @Nullable LocalTime startTime,
                                               @Nullable LocalDate endDate, @Nullable LocalTime endTime) {
        int userId = SecurityUtil.authUserId();
        List<Meal> mealsDateFiltered = mealService.getBetweenDates(startDate, endDate, userId);
        LOG.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);
        return MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }
}