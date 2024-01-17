package ru.caloriemanager.web;

import ru.caloriemanager.model.Meal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

public class MealTestData {
    public static final Map<Meal, Integer> USERS_MEALS = new HashMap<>() {{
        put(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Завтрак", 500), UserTestData.USER_ID);
        put(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Обед", 1000), UserTestData.USER_ID);
        put(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Ужин", 500), UserTestData.USER_ID);
        put(new Meal(LocalDateTime.of(2015, Month.MAY, 12, 0, 0), "Завтрак", 1000), UserTestData.USER_ID);
        put(new Meal(LocalDateTime.of(2015, Month.MAY, 12, 12, 0), "Обед", 510), UserTestData.USER_ID);
        put(new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510), UserTestData.ADMIN_ID);
        put(new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 21, 0), "Админ ужин", 1500), UserTestData.ADMIN_ID);
    }};
}
