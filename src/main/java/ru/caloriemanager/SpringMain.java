package ru.caloriemanager;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.caloriemanager.model.MealTo;
import ru.caloriemanager.model.Role;
import ru.caloriemanager.model.User;
import ru.caloriemanager.repository.JdbcMealRepository;
import ru.caloriemanager.repository.JdbcUserRepository;
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
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ROLE_ADMIN));

            MealRestController mealRestController = appCtx.getBean(MealRestController.class);

            mealRestController.get(SecurityUtil.authUserId());
            mealRestController.getAll();

            List<MealTo> filteredMealsWithExcess =
                    mealRestController.getBetween(
                            LocalDate.of(2015, Month.MAY, 30), LocalTime.of(7, 0),
                            LocalDate.of(2015, Month.MAY, 31), LocalTime.of(11, 0));
            filteredMealsWithExcess.forEach(System.out::println);

            MealRepository jdbcMealRepository = appCtx.getBean(JdbcMealRepository.class);
            System.out.println(jdbcMealRepository.getAll(100000));

            UserRepository jdbcUserRepository = appCtx.getBean(JdbcUserRepository.class);
            System.out.println(jdbcUserRepository.get(100001));
        }
    }
}
