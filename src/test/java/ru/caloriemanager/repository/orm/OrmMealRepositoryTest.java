package ru.caloriemanager.repository.orm;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.caloriemanager.model.Meal;
import ru.caloriemanager.model.Role;
import ru.caloriemanager.model.User;
import ru.caloriemanager.repository.MealRepository;

import javax.annotation.processing.Generated;
import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:spring/spring-app.xml")
class OrmMealRepositoryTest {

    private static MealRepository SUT;
    private static int CURRENT_USER_ID = 0;

    @BeforeAll
    static void init(@Autowired MealRepository ormMealRepository) {
        SUT = ormMealRepository;
        try (ConfigurableApplicationContext appCtx =
                     new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            DataSource dataSource = (DataSource) appCtx.getBean("dataSource");
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.addScripts(new ClassPathResource("db/clearMeals.sql"));
            populator.addScripts(new ClassPathResource("db/addUsers.sql"));
            populator.execute(dataSource);
        }
    }

    @BeforeEach
    void userIdIncrement() {
        CURRENT_USER_ID++;
    }

    @Test
    void saveNew() {
        //Arrange
        Meal meal = new Meal(null, "meal test", 1000);
        User user = new User(CURRENT_USER_ID, null, null, null, Role.ROLE_USER);
        meal.setUser(user);
        //Act
        Meal actual = SUT.save(meal, CURRENT_USER_ID);
        Meal checkWrite = SUT.get(meal.getId(), CURRENT_USER_ID);
        //Assert
        Assertions.assertFalse(meal.isNew());
        Assertions.assertEquals(meal, actual);
        Assertions.assertEquals(meal, checkWrite);
    }

    @Test
    void saveUpdate() {
        //Arrange
        User user = new User(CURRENT_USER_ID, null, null, null, Role.ROLE_USER);
        Meal meal = new Meal(null, "meal test", 1000);
        meal.setUser(user);
        Meal mealUpdate = new Meal(null, "meal test update", 2000);
        mealUpdate.setUser(user);
        //Act
        SUT.save(meal, CURRENT_USER_ID);
        mealUpdate.setId(meal.getId());
        Meal actual = SUT.save(mealUpdate, CURRENT_USER_ID);
        //Assert
        Assertions.assertEquals(mealUpdate, actual);
        Assertions.assertEquals(mealUpdate, SUT.get(mealUpdate.getId(), CURRENT_USER_ID));
    }

    @Test
    void delete() {
        //Arrange
        Meal meal = new Meal(null, "meal test", 1000);
        User user = new User(CURRENT_USER_ID, null, null, null, Role.ROLE_USER);
        meal.setUser(user);
        SUT.save(meal, CURRENT_USER_ID);
        //Act
        boolean result = SUT.delete(meal.getId(), CURRENT_USER_ID);
        //Assert
        assertTrue(result);
        Assertions.assertNull(SUT.get(meal.getId(), CURRENT_USER_ID));
    }

    @Test
    void get() {
        //Arrange
        Meal meal = new Meal(null, "meal test", 1000);
        User user = new User(CURRENT_USER_ID, null, null, null, Role.ROLE_USER);
        meal.setUser(user);
        SUT.save(meal, CURRENT_USER_ID);
        //Act
        Meal result = SUT.get(meal.getId(), CURRENT_USER_ID);
        //Assert
        Assertions.assertEquals(meal, result);
    }

    @Test
    void getAll() {
        //Arrange
        User user = new User(CURRENT_USER_ID, null, null, null, Role.ROLE_USER);
        Meal meal1 = new Meal(null, "meal1", 1000);
        Meal meal2 = new Meal(null, "meal2", 1000);
        Meal meal3 = new Meal(null, "meal3", 1000);
        meal1.setUser(user);
        meal2.setUser(user);
        meal3.setUser(user);
        List<Meal> mealList = Arrays.asList(meal1, meal2, meal3);
        mealList.forEach((el) -> SUT.save(el, CURRENT_USER_ID));
        Collections.reverse(mealList);
        //Act
        List<Meal> actual = SUT.getAll(CURRENT_USER_ID);
        //Assert
        Assertions.assertEquals(mealList, actual);
    }

    @Test
    void getBetween() {
        //Arrange
        User user = new User(CURRENT_USER_ID, null, null, null, Role.ROLE_USER);
        LocalDateTime dateTime1 = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(23, 59));
        LocalDateTime dateTime2 = LocalDateTime.of(LocalDate.now().minusDays(2), LocalTime.MIN);
        LocalDateTime dateTime3 = LocalDateTime.of(LocalDate.now().minusDays(3), LocalTime.of(0, 1));
        Meal meal1 = new Meal(dateTime1,"test1", 1000);
        Meal meal2 = new Meal(dateTime2,"test2", 1000);
        Meal meal3 = new Meal(dateTime3,"test3", 1000);
        meal1.setUser(user);
        meal2.setUser(user);
        meal3.setUser(user);
        SUT.save(meal1, CURRENT_USER_ID);
        SUT.save(meal2, CURRENT_USER_ID);
        SUT.save(meal3, CURRENT_USER_ID);
        List<LocalDateTime> expectedDateTime = Arrays.asList(dateTime1, dateTime2, dateTime3);
        //Act
        List<Meal> result = SUT.getBetween(dateTime3, dateTime1, CURRENT_USER_ID);
        List<LocalDateTime> actual = result.stream().map(el -> el.getDateTime()).toList();
        //Assert
        Assertions.assertEquals(expectedDateTime, actual);
    }

}