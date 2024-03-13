package ru.caloriesmanager.web.user;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.caloriesmanager.entity.Role;
import ru.caloriesmanager.entity.User;
import ru.caloriesmanager.service.CustomUserDetails;
import ru.caloriesmanager.util.exception.NotFoundException;
import javax.sql.DataSource;
import java.time.ZoneId;
import java.util.Set;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:spring/spring-app.xml",
        "classpath:spring/web/spring-mvc.xml" })
class ProfileRestControllerTest {
    //Arrange
    //Act
    //Assert
    private static ProfileRestController SUT;
    private static AdminRestController adminRestController;

    @BeforeAll
    static void init(@Autowired ProfileRestController profileRestController,
                     @Autowired AdminRestController adminRController,
                     @Autowired DataSource dataSource,
                     @Autowired SessionFactory sessionFactory) {
        SUT = profileRestController;
        adminRestController = adminRController;
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScripts(new ClassPathResource("db/clearUsersMealsRolesAndAddTestData.sql"));
        populator.execute(dataSource);
        sessionFactory.getCache().evictAllRegions();
    }

    @BeforeEach
    void deAuthenticationUser() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Test
    void get() {
        //Arrange
        User user = new User(1, "user1", "user1@yandex.ru",
                "password", 1000, true, Set.of(Role.ROLE_USER));

        CustomUserDetails userDetails = new CustomUserDetails(user);
        userDetails.setZoneId(ZoneId.of("Europe/Moscow"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //Act
        User actual = SUT.get();
        //Assert
        Assertions.assertEquals(user, actual);
    }

    @Test
    void delete() {
        //Arrange
        User user = new User(2, "user2", "user2@yandex.ru",
                "password", 1000, true, Set.of(Role.ROLE_USER));

        CustomUserDetails userDetails = new CustomUserDetails(user);
        userDetails.setZoneId(ZoneId.of("Europe/Moscow"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //Act
        SUT.delete();
        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> adminRestController.get(user.getId()));
        //Assert
        Assertions.assertEquals("Not found entity with id=" + user.getId(), exception.getMessage());
    }

    @Test
    void update() {
        //Arrange
        User user = new User(3, "user3", "user3@yandex.ru",
                "password", 1000, true, Set.of(Role.ROLE_USER));

        CustomUserDetails userDetails = new CustomUserDetails(user);
        userDetails.setZoneId(ZoneId.of("Europe/Moscow"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User userInBase = SUT.get();
        userInBase.setName("update");
        userInBase.setCaloriesPerDay(1500);
        //Act
        SUT.update(userInBase);
        User actual = SUT.get();
        //Assert
        Assertions.assertEquals(userInBase, actual);
    }
}