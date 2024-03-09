package ru.caloriesmanager.web.meal;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.caloriesmanager.entity.Meal;
import ru.caloriesmanager.model.MealViewModel;
import ru.caloriesmanager.model.MealWithExcessModel;
import ru.caloriesmanager.service.CustomUserDetailsService;
import ru.caloriesmanager.service.MealService;
import ru.caloriesmanager.util.DateTimeUtil;
import ru.caloriesmanager.util.MealsUtil;
import java.time.*;
import java.util.List;


@Controller
@RequestMapping("/meals")
public class MealController {

    @Autowired
    private MealService mealService;

    @RequestMapping("/meals")
    public String getAllMeals(Model model) {
        int userCaloriesPerDay = CustomUserDetailsService.getCustomUserDetails().getUser().getCaloriesPerDay();
        int userId = CustomUserDetailsService.getCustomUserDetails().getUser().getId();
        List<Meal> mealsDateFiltered =
                mealService.getBetweenDates(DateTimeUtil.MIN_DATE, DateTimeUtil.MAX_DATE, userId);
        List<MealWithExcessModel> filteredMealToList =
                MealsUtil.getFilteredTos(mealsDateFiltered, userCaloriesPerDay, LocalTime.MIN, LocalTime.MAX);
        model.addAttribute("meals", filteredMealToList);
        return "meals";
    }

    @RequestMapping("/create")
    public String createMeal(Model model) {
        ZoneId zoneId = CustomUserDetailsService.getCustomUserDetails().getZoneId();
        ZonedDateTime systemZoned = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault());
        ZonedDateTime userZoned = systemZoned.withZoneSameInstant(zoneId);
        MealViewModel mealViewModel = new MealViewModel(null, userZoned.toLocalDateTime(), "", 1000);
        model.addAttribute("meal", mealViewModel);
        return "mealForm";
    }

    @RequestMapping("/update")
    public String updateMeal(@RequestParam("id") int mealId, Model model) {
        int userId = CustomUserDetailsService.getCustomUserDetails().getUser().getId();
        Meal meal = mealService.get(mealId, userId);
        model.addAttribute("meal", MealViewModel.getModel(meal));
        return "mealForm";
    }

    @RequestMapping("/delete")
    public String deleteMeal(@RequestParam("id") int mealId) {
        int userId = CustomUserDetailsService.getCustomUserDetails().getUser().getId();
        mealService.delete(mealId, userId);
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

        ZoneId zoneId = CustomUserDetailsService.getCustomUserDetails().getZoneId();
        ZonedDateTime startUserZoned = ZonedDateTime.of(startD, LocalTime.MIN, zoneId);
        ZonedDateTime endUserZoned = ZonedDateTime.of(endD, LocalTime.MAX, zoneId);
        ZonedDateTime startSystemZoned = startUserZoned.withZoneSameInstant(ZoneId.systemDefault());
        ZonedDateTime endSystemZoned = endUserZoned.withZoneSameInstant(ZoneId.systemDefault());

        int userCaloriesPerDay = CustomUserDetailsService.getCustomUserDetails().getUser().getCaloriesPerDay();
        int userId = CustomUserDetailsService.getCustomUserDetails().getUser().getId();

        List<Meal> mealsDateFiltered =
                mealService.getBetweenDates(startSystemZoned.toLocalDate(), endSystemZoned.toLocalDate(), userId);

        mealsDateFiltered.forEach(el -> {
            ZonedDateTime systemZoned = ZonedDateTime.of(el.getDateTime(), ZoneId.systemDefault());
            ZonedDateTime userZoned = systemZoned.withZoneSameInstant(zoneId);
            el.setDateTime(userZoned.toLocalDateTime());
        });

        List<MealWithExcessModel> filteredMealToList =
                MealsUtil.getFilteredTos(mealsDateFiltered, userCaloriesPerDay, startT, endT);

        model.addAttribute("meals", filteredMealToList);
        return "meals";
    }

    @RequestMapping("/edit")
    public String editMeal(@Valid @ModelAttribute("meal") MealViewModel mealViewModel,
                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "mealForm";
        }

        int userId = CustomUserDetailsService.getCustomUserDetails().getUser().getId();
        ZoneId zoneId = CustomUserDetailsService.getCustomUserDetails().getZoneId();
        ZonedDateTime userZoned = ZonedDateTime.of(mealViewModel.getDateTime(), zoneId);
        ZonedDateTime systemZoned = userZoned.withZoneSameInstant(ZoneId.systemDefault());
        mealViewModel.setDateTime(systemZoned.toLocalDateTime());
        Meal meal = MealViewModel.getMealInstance(mealViewModel);

        if (meal.isNew())
            mealService.create(meal, userId);
        else
            mealService.update(meal, userId);

        return "redirect:/meals/meals";
    }
}
