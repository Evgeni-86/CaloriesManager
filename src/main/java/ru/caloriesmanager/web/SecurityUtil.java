package ru.caloriesmanager.web;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.caloriesmanager.entity.User;
import ru.caloriesmanager.service.CustomUserDetails;

import java.time.ZoneId;


public class SecurityUtil {
    private static int userId = 1;
//    public static final int DEFAULT_CALORIES_PER_DAY = 2000;
//    public static ZoneId zoneId = ZoneId.of("Europe/Moscow");


//    public static CustomUserDetails getCustomUserDetails() {
//        UsernamePasswordAuthenticationToken authentication =
//                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
//        validatePrinciple(authentication.getPrincipal());
//        return (CustomUserDetails) authentication.getPrincipal();
//    }
//    private static void validatePrinciple(Object principal) {
//        if (!(principal instanceof CustomUserDetails)) {
//            throw new  IllegalArgumentException("Principal can not be null!");
//        }
//    }

    public static void setUserId(int userId) {
        SecurityUtil.userId = userId;
    }

    public static int authUserId() {
        return userId;
    }

//    public static int authUserCaloriesPerDay() {
//        return DEFAULT_CALORIES_PER_DAY;
//    }
}