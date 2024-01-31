package ru.caloriemanager;

import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.caloriemanager.model.Meal;
import ru.caloriemanager.service.MealService;

import java.time.LocalDateTime;
import java.util.Arrays;


public class SpringMain {
    public static void main(String[] args) {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        try (ConfigurableApplicationContext appCtx =
                     new ClassPathXmlApplicationContext("spring/spring-app.xml")) {

//            Arrays.stream(appCtx.getBeanDefinitionNames()).forEach(el-> System.out.println(el));

//            Meal meal = new Meal(LocalDateTime.now(), "Test", 1000);
//
//            MealService mealService = appCtx.getBean("mealService", MealService.class);
//            mealService.create(meal, 1);
        }
    }
}
