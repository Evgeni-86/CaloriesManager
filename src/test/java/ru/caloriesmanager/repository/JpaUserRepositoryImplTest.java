package ru.caloriesmanager.repository;

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

import javax.sql.DataSource;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:spring/spring-app.xml")
class JpaUserRepositoryImplTest {

    private static UserRepository SUT;

    @BeforeAll
    static void init(@Autowired UserRepository userRepository, @Autowired DataSource dataSource) {
        SUT = userRepository;
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScripts(new ClassPathResource("db/clearUsersMealsRolesTables.sql"));
        populator.execute(dataSource);
    }


    @Test
    void saveNew() {
        //Arrange
        User user = new User(null, "new user", "saveNew@email",
                "pass", 2000, true, Set.of(Role.ROLE_USER));
        //Act
        User actual = SUT.save(user);
        User checkWrite = SUT.get(user.getId());
        //Assert
        Assertions.assertFalse(user.isNew());
        Assertions.assertEquals(user, actual);
        Assertions.assertEquals(user, checkWrite);
    }

    @Test
    void saveUpdate() {
        //Arrange
        User user = new User(null, "new user", "saveUpdate@email",
                "pass", 2000, true, Set.of(Role.ROLE_USER));
        SUT.save(user);
        user.setName("update name");
        user.setCaloriesPerDay(1500);
        //Act
        User actual = SUT.save(user);
        User checkWrite = SUT.get(user.getId());
        //Assert
        Assertions.assertFalse(user.isNew());
        Assertions.assertEquals(user, actual);
        Assertions.assertEquals(user, checkWrite);
    }

    @Test
    void delete() {
        //Arrange
        User user = new User(null, "new user", "delete@email",
                "pass", 2000, true, Set.of(Role.ROLE_USER));
        SUT.save(user);
        //Act
        boolean actual = SUT.delete(user.getId());
        //Assert
        assertTrue(actual);
        Assertions.assertNull(SUT.get(user.getId()));
    }

    @Test
    void get() {
        //Arrange
        User user = new User(null, "new user", "get@email",
                "pass", 2000, true, Set.of(Role.ROLE_USER));
        SUT.save(user);
        //Act
        User actual = SUT.get(user.getId());
        //Assert
        Assertions.assertEquals(user, actual);
    }

    @Test
    void getByEmail() {
        //Arrange
        User user = new User(null, "new user", "getByEmail@email",
                "pass", 2000, true, Set.of(Role.ROLE_USER));
        SUT.save(user);
        //Act
        User actual = SUT.getByEmail("getByEmail@email");
        //Assert
        Assertions.assertEquals(user, actual);
    }

    @Sql("classpath:db/clearUsersMealsRolesTables.sql")
    @Test
    void getAll() {
        //Arrange
        User user1 = new User(null, "new user1", "user1@email",
                "pass", 2000, true, Set.of(Role.ROLE_USER));
        User user2 = new User(null, "new user2", "user2@email",
                "pass", 2000, true, Set.of(Role.ROLE_USER));
        User user3 = new User(null, "new user3", "user3@email",
                "pass", 2000, true, Set.of(Role.ROLE_USER));
        SUT.save(user1);
        SUT.save(user2);
        SUT.save(user3);
        //Act
        List<User> actual = SUT.getAll();
        //Assert
        Assertions.assertEquals(List.of(user1, user2, user3), actual);
    }
}