package ru.caloriemanager.web.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.caloriemanager.model.Role;
import ru.caloriemanager.model.User;
import ru.caloriemanager.util.exception.NotFoundException;
import ru.caloriemanager.web.SecurityUtil;

import javax.sql.DataSource;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:spring/spring-app.xml")
class ProfileRestControllerTest {
    //Arrange
    //Act
    //Assert

    private static ProfileRestController SUT;

    @BeforeAll
    static void init(@Autowired ProfileRestController profileRestController,
                     @Autowired DataSource dataSource) {
        SUT = profileRestController;
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScripts(new ClassPathResource("db/clearUsersMealsRolesAndAddTestData.sql"));
        populator.execute(dataSource);
    }

    @Test
    void get() {
        //Arrange
        SecurityUtil.setUserId(1);
        User user = new User(1, "user1", "user1@yandex.ru",
                "password", 1000, true, Set.of(Role.ROLE_USER));
        //Act
        User actual = SUT.get();
        //Assert
        Assertions.assertEquals(user, actual);
    }

    @Test
    void delete() {
        //Arrange
        SecurityUtil.setUserId(2);
        User user = SUT.get();
        //Act
        SUT.delete();
        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> SUT.get());
        //Assert
        Assertions.assertEquals("Not found entity with id=" + user.getId(), exception.getMessage());
    }

    @Test
    void update() {
        //Arrange
        SecurityUtil.setUserId(3);
        User user = SUT.get();
        user.setName("update");
        user.setCaloriesPerDay(1500);
        //Act
        SUT.update(user);
        User actual = SUT.get();
        //Assert
        Assertions.assertEquals(user, actual);
    }
}