package ru.caloriemanager;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SpringMain {
    public static void main(String[] args) {

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
