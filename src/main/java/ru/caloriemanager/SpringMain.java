package ru.caloriemanager;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import ru.caloriemanager.model.MealTo;
import ru.caloriemanager.model.Role;
import ru.caloriemanager.model.User;
import ru.caloriemanager.repository.jdbc.JdbcMealRepository;
import ru.caloriemanager.repository.jdbc.JdbcUserRepository;
import ru.caloriemanager.repository.MealRepository;
import ru.caloriemanager.repository.UserRepository;
import ru.caloriemanager.web.SecurityUtil;
import ru.caloriemanager.web.meal.MealRestController;
import ru.caloriemanager.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;


public class SpringMain {
    public static void main(String[] args) {
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);

            LocalContainerEntityManagerFactoryBean managerFactory =
                    appCtx.getBean(LocalContainerEntityManagerFactoryBean.class, "emf");
            System.out.println(managerFactory.getJpaPropertyMap());
        }
    }
}
