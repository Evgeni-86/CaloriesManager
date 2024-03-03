package ru.caloriesmanager;



import org.hibernate.cfg.AvailableSettings;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;



public class SpringMain {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "hsqldb, appDataSource");
        try (ConfigurableApplicationContext appCtx =
                     new ClassPathXmlApplicationContext("spring/spring-app.xml")) {

        }
    }
}
