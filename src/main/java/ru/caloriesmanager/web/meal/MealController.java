package ru.caloriesmanager.web.meal;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.caloriesmanager.model.Meal;
import ru.caloriesmanager.service.MealService;
import ru.caloriesmanager.transferObject.UserMealWithExcess;
import ru.caloriesmanager.util.DateTimeUtil;
import ru.caloriesmanager.util.MealsUtil;
import ru.caloriesmanager.web.SecurityUtil;
import ru.caloriesmanager.web.meal.MealRestController;

import java.net.http.HttpRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
        List<UserMealWithExcess> filteredMealToList =
                MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), LocalTime.MIN, LocalTime.MAX);
        model.addAttribute("meals", filteredMealToList);
        return "meals";
    }

    @RequestMapping("/create")
    public String createMeal(Model model) {
        Meal meal = new Meal(LocalDateTime.now(), "", 1000);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @RequestMapping("/update")
    public String updateMeal(@RequestParam("id") int mealId, Model model) {
        Meal meal = mealService.get(mealId, SecurityUtil.authUserId());
        model.addAttribute("meal", meal);
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
        List<UserMealWithExcess> filteredMealToList =
                MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startT, endT);
        model.addAttribute("meals", filteredMealToList);
        return "meals";
    }

    @RequestMapping("/edit")
    public String editMeal(@RequestParam("id") String mealId,
                           @RequestParam("dateTime") String dTime,
                           @RequestParam("description") String description,
                           @RequestParam("calories") int calories) {
        LocalDateTime dateTime = LocalDateTime.parse(dTime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        Meal meal = new Meal(mealId.isEmpty() ? null : Integer.parseInt(mealId), dateTime, description, calories);
        if (meal.isNew())
            mealService.create(meal, SecurityUtil.authUserId());
        else
            mealService.update(meal, SecurityUtil.authUserId());
        return "redirect:/meals/meals";
    }
}
