package ru.caloriemanager.repository.jdbc;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.caloriemanager.model.Role;
import ru.caloriemanager.model.User;

import javax.sql.DataSource;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:spring/spring-app.xml")
class JdbcUserRepositorySpringTest {

    private static JdbcUserRepository SUT;
    private static JdbcTemplate jdbcTemplate;

    @BeforeAll
    static void init(@Autowired JdbcUserRepository jdbcUserRepository,
                     @Autowired @Qualifier("dataSource") DataSource dataSource) {
        SUT = jdbcUserRepository;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @AfterEach
    void destroy(@Autowired @Qualifier("dataSource") DataSource dataSource) {
        String sql = "DELETE FROM users";
        jdbcTemplate.execute(sql);
    }

    @Test
    void saveNew() {
        //Arrange
        User user = new User(null, "name", "email@email", "pass",
                2000, true, new TreeSet<>(List.of(Role.ROLE_USER)));
        //Act
        User result = SUT.save(user);
        //Assert
        Assertions.assertFalse(user.isNew());
        Assertions.assertEquals(user, result);
        Assertions.assertEquals(user, SUT.get(user.getId()));
    }

    @Test
    void saveUpdate() {
        //Arrange
        User user = new User(null, "name", "email@email", "pass",
                2000, true, new TreeSet<>(List.of(Role.ROLE_USER)));
        User userUpdate = new User(null, "nameUpdate", "email@emailUpdate", "passUpdate",
                3000, true, new TreeSet<>(List.of(Role.ROLE_USER)));
        //Act
        SUT.save(user);
        userUpdate.setId(user.getId());
        User result = SUT.save(userUpdate);
        //Assert
        Assertions.assertEquals(userUpdate, result);
        Assertions.assertEquals(userUpdate, SUT.get(userUpdate.getId()));
    }

    @Test
    void delete() {
        //Arrange
        User user = new User(null, "name", "email@email", "pass",
                2000, true, new TreeSet<>(List.of(Role.ROLE_USER)));
        SUT.save(user);
        //Act
        boolean result = SUT.delete(user.getId());
        //Assert
        Assertions.assertTrue(result);
        assertThrows(RuntimeException.class, () -> SUT.get(user.getId()));
    }

    @Test
    void get() {
        //Arrange
        User user = new User(null, "name", "email@email", "pass",
                2000, true, new TreeSet<>(List.of(Role.ROLE_USER)));
        SUT.save(user);
        //Act
        User result = SUT.get(user.getId());
        //Assert
        Assertions.assertEquals(user, result);
        assertThat(result).usingRecursiveComparison()
                .ignoringFields( "registered", "roles").isEqualTo(user);
    }

    @Test
    void getByEmail() {
        //Arrange
        User user = new User(null, "name", "email@email", "pass",
                2000, true, new TreeSet<>(List.of(Role.ROLE_USER)));
        SUT.save(user);
        //Act
        User result = SUT.getByEmail("email@email");
        //Assert
        Assertions.assertEquals(user, result);
    }

    @Test
    void getAll() {
        //Arrange
        User user1 = new User(null, "name", "email1@email", "pass",
                2000, true, new TreeSet<>(List.of(Role.ROLE_USER)));
        User user2 = new User(null, "name", "email2@email", "pass",
                2000, true, new TreeSet<>(List.of(Role.ROLE_USER)));
        User user3 = new User(null, "name", "email3@email", "pass",
                2000, true, new TreeSet<>(List.of(Role.ROLE_USER)));
        List<User> userList = Arrays.asList(user1, user3, user2);
        userList.forEach((el) -> SUT.save(el));
        //Act
        List<User> result = SUT.getAll();
        //Assert
        Assertions.assertEquals(userList, result);
    }
}