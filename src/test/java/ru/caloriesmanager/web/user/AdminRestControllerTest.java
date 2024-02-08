package ru.caloriesmanager.web.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.caloriesmanager.entity.Role;
import ru.caloriesmanager.entity.User;
import ru.caloriesmanager.util.exception.NotFoundException;

import javax.sql.DataSource;
import java.util.List;
import java.util.Set;


@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:spring/spring-app.xml")
class AdminRestControllerTest {
    //Arrange
    //Act
    //Assert

    private static AdminRestController SUT;

    @BeforeAll
    static void init(@Autowired AdminRestController adminRestController,
                     @Autowired DataSource dataSource) {
        SUT = adminRestController;
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScripts(new ClassPathResource("db/clearUsersMealsRolesTables.sql"));
        populator.execute(dataSource);
    }

    @Test
    void create() {
        //Arrange
        User user = new User(null, "new user", "create@email",
                "pass", 2000, true, Set.of(Role.ROLE_USER));
        //Act
        //Assert
        Assertions.assertEquals(user, SUT.create(user));
        Assertions.assertEquals(user, SUT.get(user.getId()));
    }

    @Test
    void createExceptionTest() {
        //Arrange
        User user = new User(null, "new user", "createExceptionTest@email",
                "pass", 2000, true, Set.of(Role.ROLE_USER));
        SUT.create(user);
        //Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> SUT.create(user));
        //Assert
        Assertions.assertEquals(IllegalArgumentException.class, exception.getClass());
        Assertions.assertEquals(user + " must be new (id=null)", exception.getMessage());
    }

    @Test
    void update() {
        //Arrange
        User user = new User(null, "new user", "update@email",
                "pass", 2000, true, Set.of(Role.ROLE_USER));
        SUT.create(user);
        user.setName("update user");
        user.setCaloriesPerDay(1500);
        //Act
        SUT.update(user, user.getId());
        //Assert
        Assertions.assertEquals(user, SUT.get(user.getId()));
    }

    @Test
    void get() {
        //Arrange
        User user = new User(null, "new user", "get@email",
                "pass", 2000, true, Set.of(Role.ROLE_USER));
        //Act
        //Assert
        Assertions.assertEquals(user, SUT.create(user));
        Assertions.assertEquals(user, SUT.get(user.getId()));
    }

    @Test
    void getExceptionTest() {
        //Arrange
        User user = new User(Integer.MAX_VALUE, "new user", "getExceptionTest@email",
                "pass", 2000, true, Set.of(Role.ROLE_USER));
        //Act
        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> SUT.get(Integer.MAX_VALUE));
        //Assert
        Assertions.assertEquals(NotFoundException.class, exception.getClass());
        Assertions.assertEquals("Not found entity with id=" + user.getId(), exception.getMessage());
    }

    @Test
    void delete() {
        //Arrange
        User user = new User(null, "new user", "delete@email",
                "pass", 2000, true, Set.of(Role.ROLE_USER));
        SUT.create(user);
        //Act
        SUT.delete(user.getId());
        //Assert
        Assertions.assertThrows(NotFoundException.class, () -> SUT.get(user.getId()));
    }

    @Test
    void deleteExceptionTest() {
        //Arrange
        //Act
        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> SUT.delete(Integer.MAX_VALUE));
        //Assert
        Assertions.assertEquals("Not found entity with id=" + Integer.MAX_VALUE, exception.getMessage());
    }


    @Test
    void getByMail() {
        //Arrange
        User user = new User(null, "new user", "getByMail@email",
                "pass", 2000, true, Set.of(Role.ROLE_USER));
        SUT.create(user);
        //Act
        User actual = SUT.getByMail("getByMail@email");
        //Assert
        Assertions.assertEquals(user, actual);
    }

    @Test
    void getByMailExceptionTest() {
        //Arrange
        //Act
        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> SUT.getByMail("@@"));
        //Assert
        Assertions.assertEquals("Not found entity with email=@@", exception.getMessage());
    }

    @Sql("classpath:db/clearUsersMealsRolesTables.sql")
    @Test
    void getALL() {
        //Arrange
        User user1 = new User(null, "new user1", "user1@email",
                "pass", 2000, true, Set.of(Role.ROLE_USER));
        User user2 = new User(null, "new user2", "user2@email",
                "pass", 2000, true, Set.of(Role.ROLE_USER));
        User user3 = new User(null, "new user3", "user3@email",
                "pass", 2000, true, Set.of(Role.ROLE_USER));
        SUT.create(user1);
        SUT.create(user2);
        SUT.create(user3);
        //Act
        List<User> actual = SUT.getAll();
        //Assert
        Assertions.assertEquals(List.of(user1, user2, user3), actual);
    }
}