package ru.caloriesmanager;

import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.caloriesmanager.util.AppCacheManager;
import ru.caloriesmanager.model.Role;
import ru.caloriesmanager.model.User;

import javax.cache.Cache;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class SpringMain {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "hsqldb");
        try (ConfigurableApplicationContext appCtx =
                     new ClassPathXmlApplicationContext("spring/spring-app.xml")) {


        }
    }
}
