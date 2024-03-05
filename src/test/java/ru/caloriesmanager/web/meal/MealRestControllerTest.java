package ru.caloriesmanager.web.meal;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.caloriesmanager.entity.Role;
import ru.caloriesmanager.entity.User;
import ru.caloriesmanager.model.MealViewModel;
import ru.caloriesmanager.model.MealWithExcessModel;
import ru.caloriesmanager.util.exception.NotFoundException;
import ru.caloriesmanager.web.SecurityUtil;
import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;



@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:spring/spring-app.xml")
class MealRestControllerTest {
    private static MealRestController SUT;
    private static User user;

    @BeforeAll
    static void init(@Autowired MealRestController controller,
                     @Autowired DataSource dataSource,
                     @Autowired SessionFactory sessionFactory) {
        SUT = controller;
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScripts(new ClassPathResource("db/clearUsersMealsRolesAndAddTestData.sql"));
        populator.execute(dataSource);
        sessionFactory.getCache().evictAllRegions();
    }

    @BeforeEach
    void initUser() {
        SecurityUtil.setUserId(1);
        user = new User(1, null, null, null, Role.ROLE_USER);
    }

    @Test
    void create() {
        //Arrange
        MealViewModel mealViewModel = new MealViewModel(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        //Act
        MealViewModel actual = SUT.create(mealViewModel);
        MealViewModel fromDataBase = SUT.get(actual.getId());
        //Assert
        assertThat(actual).usingRecursiveComparison().ignoringFields("id").isEqualTo(mealViewModel);
        assertThat(actual).usingRecursiveComparison().isEqualTo(fromDataBase);
    }

    @Test
    void update() {
        //Arrange
        MealViewModel model = new MealViewModel(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        MealViewModel mealViewModel = SUT.create(model);
        mealViewModel.setDescription("update");
        mealViewModel.setCalories(100);
        //Act
        SUT.update(mealViewModel);
        MealViewModel fromDataBase = SUT.get(mealViewModel.getId());
        //Assert
        assertThat(mealViewModel).usingRecursiveComparison().isEqualTo(fromDataBase);
    }

    @Test
    void get() {
        //Arrange
        MealViewModel mealViewModel = new MealViewModel(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        MealViewModel model = SUT.create(mealViewModel);
        //Act
        MealViewModel actual = SUT.get(model.getId());
        //Assert
        assertThat(actual).usingRecursiveComparison().ignoringFields("id").isEqualTo(mealViewModel);
        assertThat(actual).usingRecursiveComparison().isEqualTo(model);
    }

    @Test
    void delete() {
        //Arrange
        MealViewModel mealViewModel = new MealViewModel(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        MealViewModel model = SUT.create(mealViewModel);
        //Act
        SUT.delete(model.getId());
        //Assert
        Assertions.assertThrows(NotFoundException.class, () -> SUT.get(model.getId()));
    }

    @Test
    void getAll() {
        //Arrange
        SecurityUtil.setUserId(2);

        MealViewModel mealViewModel1 = new MealViewModel(LocalDateTime.of(2017, Month.MAY,
                12, 0, 0), "Завтрак", 500);
        MealViewModel mealViewModel2 = new MealViewModel(LocalDateTime.of(2017, Month.MAY,
                12, 1, 0), "Обед", 500);
        MealViewModel mealViewModel3 = new MealViewModel(LocalDateTime.of(2017, Month.MAY,
                12, 2, 0), "Обед", 500);

        MealViewModel model1 = SUT.create(mealViewModel1);
        MealViewModel model2 = SUT.create(mealViewModel2);
        MealViewModel model3 = SUT.create(mealViewModel3);
        //Act
        List<MealWithExcessModel> actual = SUT.getAll();
        //Assert
        Assertions.assertNotNull(actual);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("excess").isEqualTo(List.of(model3, model2, model1));
    }

    @Test
    void getBetween() {
        //Arrange
        SecurityUtil.setUserId(3);

        MealViewModel mealViewModel1 = new MealViewModel(LocalDateTime.of(2017, Month.MAY,
                12, 0, 0), "Завтрак", 500);
        MealViewModel mealViewModel2 = new MealViewModel(LocalDateTime.of(2017, Month.MAY,
                12, 12, 0), "Обед", 500);
        MealViewModel model1 = SUT.create(mealViewModel1);
        MealViewModel model2 = SUT.create(mealViewModel2);
        //Act
        List<MealWithExcessModel> actual = SUT.getBetween(
                new HashMap<>() {{
                    put("startDate", "2017-05-12");
                    put("endDate", "2017-05-12");
                    put("startTime", "00:00:00");
                    put("endTime", "12:00:00");
                }});
        //Assert
        Assertions.assertNotNull(actual);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("excess").isEqualTo(List.of(model2, model1));
    }
}