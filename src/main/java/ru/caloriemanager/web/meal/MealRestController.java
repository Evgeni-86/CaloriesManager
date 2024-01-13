package ru.caloriemanager.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.caloriemanager.model.Meal;
import ru.caloriemanager.model.MealTo;
import ru.caloriemanager.service.MealService;
import ru.caloriemanager.util.MealsUtil;
import ru.caloriemanager.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {
    private static final Logger LOG = LoggerFactory.getLogger(MealService.class);
    @Autowired
    public MealService mealService;

    public Meal create(Meal meal) {
        LOG.info("user {} create meal {}", SecurityUtil.authUserId(), meal);
        return mealService.create(meal, SecurityUtil.authUserId());
    }

    public void update(Meal meal) {
        LOG.info("user {} update meal {}", SecurityUtil.authUserId(), meal);
        mealService.update(meal, SecurityUtil.authUserId());
    }

    public Meal get(int id) {
        LOG.info("user {} get meal id = {}", SecurityUtil.authUserId(), id);
        return mealService.get(id, SecurityUtil.authUserId());
    }

    public boolean delete(int id) {
        LOG.info("user {} delete meal id = {}", SecurityUtil.authUserId(), id);
        return mealService.delete(id, SecurityUtil.authUserId());
    }

    public List<MealTo> getAll() {
        LOG.info("getAll for user {}", SecurityUtil.authUserId());
        return MealsUtil.getFilteredMealsWithExceeded(mealService.getAll(SecurityUtil.authUserId()), LocalTime.MIN, LocalTime.MAX,
                SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealTo> getBetween(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        LOG.info("get Between dates {} - {} for time {} - {} for user {}", startDate, endDate, startTime, endTime, SecurityUtil.authUserId());
        return MealsUtil.getFilteredMealsWithExceeded(mealService.getBetweenDates(startDate, endDate, SecurityUtil.authUserId()),
                startTime, endTime, SecurityUtil.authUserCaloriesPerDay());
    }
}