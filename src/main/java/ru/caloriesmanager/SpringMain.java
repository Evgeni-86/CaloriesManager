package ru.caloriesmanager;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.caloriesmanager.web.MealServlet;

import java.lang.reflect.Array;
import java.util.Arrays;


public class SpringMain {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "hsqldb, appDataSource");
        try (ConfigurableApplicationContext appCtx =
                     new ClassPathXmlApplicationContext("spring/spring-app.xml")) {

        }
    }
}
