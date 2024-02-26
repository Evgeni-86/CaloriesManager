package ru.caloriesmanager.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.caloriesmanager.entity.Meal;
import ru.caloriesmanager.model.MealViewModel;
import ru.caloriesmanager.model.MealWithExcessModel;
import ru.caloriesmanager.service.MealService;
import ru.caloriesmanager.util.DateTimeUtil;
import ru.caloriesmanager.util.MealsUtil;
import ru.caloriesmanager.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RequestMapping("/meals")
@Controller
public class MealController {

    @Autowired
    private MealService mealService;

    @RequestMapping("/meals")
    public String getAllMeals(Model model) {
        List<Meal> mealsDateFiltered =
                mealService.getBetweenDates(DateTimeUtil.MIN_DATE, DateTimeUtil.MAX_DATE, SecurityUtil.authUserId());
        List<MealWithExcessModel> filteredMealToList =
                MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), LocalTime.MIN, LocalTime.MAX);
        model.addAttribute("meals", filteredMealToList);
        return "meals";
    }

    @RequestMapping("/create")
    public String createMeal(Model model) {
        MealViewModel mealViewModel = new MealViewModel(null, LocalDateTime.now(), "", 1000);
        model.addAttribute("meal", mealViewModel);
        return "mealForm";
    }

    @RequestMapping("/update")
    public String updateMeal(@RequestParam("id") int mealId, Model model) {
        Meal meal = mealService.get(mealId, SecurityUtil.authUserId());
        model.addAttribute("meal", MealViewModel.getModel(meal));
        return "mealForm";
    }

    @RequestMapping("/delete")
    public String deleteMeal(@RequestParam("id") int mealId) {
        mealService.delete(mealId, SecurityUtil.authUserId());
        return "redirect:/meals/meals";
    }

    @RequestMapping("/filter")
    public String filter(@RequestParam("startDate") String startDate,
                         @RequestParam("endDate") String endDate,
                         @RequestParam("startTime") String startTime,
                         @RequestParam("endTime") String endTime,
                         Model model) {
        LocalDate startD = DateTimeUtil.parseToLocalDate(startDate, DateTimeUtil.MIN_DATE);
        LocalDate endD = DateTimeUtil.parseToLocalDate(endDate, DateTimeUtil.MAX_DATE);
        LocalTime startT = DateTimeUtil.parseToLocalTime(startTime, LocalTime.MIN);
        LocalTime endT = DateTimeUtil.parseToLocalTime(endTime, LocalTime.MAX);
        List<Meal> mealsDateFiltered = mealService.getBetweenDates(startD, endD, SecurityUtil.authUserId());
        List<MealWithExcessModel> filteredMealToList =
                MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startT, endT);
        model.addAttribute("meals", filteredMealToList);
        return "meals";
    }

    @RequestMapping("/edit")
    public String editMeal(@ModelAttribute("meal") MealViewModel mealViewModel) {
        Meal meal = new Meal(
                mealViewModel.getId() == null ? null : mealViewModel.getId(),
                mealViewModel.getDateTime(),
                mealViewModel.getDescription(),
                mealViewModel.getCalories());
        if (meal.isNew())
            mealService.create(meal, SecurityUtil.authUserId());
        else
            mealService.update(meal, SecurityUtil.authUserId());
        return "redirect:/meals/meals";
    }
}
