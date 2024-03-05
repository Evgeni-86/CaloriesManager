package ru.caloriesmanager.web;


import java.time.ZoneId;

public class SecurityUtil {
    private static int userId = 1;
    public static final int DEFAULT_CALORIES_PER_DAY = 2000;
    public static ZoneId zoneId = ZoneId.of("Europe/Moscow");


    public static void setUserId(int userId) {
        SecurityUtil.userId = userId;
    }

    public static int authUserId() {
        return userId;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}