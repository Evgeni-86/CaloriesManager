package ru.caloriemanager.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.caloriemanager.model.Role;
import ru.caloriemanager.model.User;
import ru.caloriemanager.repository.UserRepository;
import ru.caloriemanager.util.exception.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

//@ExtendWith(SpringExtension.class)
//@ContextConfiguration("classpath:spring/spring-app.xml")
@ExtendWith(MockitoExtension.class)
class UserServiceMockTest {
    @Mock
    private UserRepository repository;
    @InjectMocks
    private UserService userService;

    private final User USER_WITH_ID = new User(1, "User name", "email@mail.ru", "pass", Role.ROLE_USER);
    private final User USER_WITHOUT_ID = new User(null, "User name", "email@mail.ru", "pass", Role.ROLE_USER);

    @Test
    void create() {
        Mockito.when(repository.save(USER_WITHOUT_ID)).thenReturn(USER_WITHOUT_ID);
        User user = userService.create(USER_WITHOUT_ID);
        Assertions.assertNotNull(user);
        Assertions.assertEquals(USER_WITHOUT_ID, user);
        Mockito.verify(repository, times(1)).save(USER_WITHOUT_ID);
    }

    @Test
    void createIllegalArgumentException() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> userService.create(USER_WITH_ID));
        Assertions.assertEquals(USER_WITH_ID + " must be new (id=null)", exception.getMessage());
        Mockito.verify(repository, times(0)).save(USER_WITH_ID);
    }

    @Test
    void update() {
        Mockito.when(repository.save(USER_WITH_ID)).thenReturn(USER_WITH_ID);
        userService.update(USER_WITH_ID);
        Mockito.verify(repository, times(1)).save(USER_WITH_ID);
    }

    @Test
    void updateNotFoundException() {
        Mockito.when(repository.save(USER_WITH_ID)).thenReturn(null);
        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> userService.update(USER_WITH_ID));
        Assertions.assertEquals("Not found entity with id=" + USER_WITH_ID.getId(), exception.getMessage());
        Mockito.verify(repository, times(1)).save(USER_WITH_ID);
    }

    @Test
    void delete() {
        Mockito.when(repository.delete(USER_WITH_ID.getId())).thenReturn(true);
        userService.delete(USER_WITH_ID.getId());
        Mockito.verify(repository, times(1)).delete(USER_WITH_ID.getId());
    }

    @Test
    void deleteNotFoundException() {
        Mockito.when(repository.delete(USER_WITH_ID.getId())).thenReturn(false);
        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> userService.delete(USER_WITH_ID.getId()));
        Assertions.assertEquals("Not found entity with id=" + USER_WITH_ID.getId(), exception.getMessage());
        Mockito.verify(repository, times(1)).delete(USER_WITH_ID.getId());
    }

    @Test
    void get() {
        Mockito.when(repository.get(USER_WITH_ID.getId())).thenReturn(USER_WITH_ID);
        User result = userService.get(USER_WITH_ID.getId());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(USER_WITH_ID, result);
        Mockito.verify(repository, times(1)).get(USER_WITH_ID.getId());
    }

    @Test
    void getNotFoundException() {
        Mockito.when(repository.get(USER_WITH_ID.getId())).thenReturn(null);
        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> userService.get(USER_WITH_ID.getId()));
        Assertions.assertEquals("Not found entity with id=" + USER_WITH_ID.getId(), exception.getMessage());
        Mockito.verify(repository, times(1)).get(USER_WITH_ID.getId());
    }

    @Test
    void getByEmail() {
        String email = "email";
        Mockito.when(repository.getByEmail(email)).thenReturn(USER_WITH_ID);
        User user = userService.getByEmail(email);
        Assertions.assertNotNull(user);
        Assertions.assertEquals(USER_WITH_ID, user);
        Mockito.verify(repository, times(1)).getByEmail(email);
    }

    @Test
    void getByEmailNotFoundException() {
        String email = "email";
        Mockito.when(repository.getByEmail(email)).thenReturn(null);
        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> userService.getByEmail(email));
        Assertions.assertEquals("Not found entity with email=" + email, exception.getMessage());
        Mockito.verify(repository, times(1)).getByEmail(email);
    }

    @Test
    void getAll() {
        List<User> list = new ArrayList<>();
        list.add(USER_WITH_ID);
        Mockito.when(repository.getAll()).thenReturn(list);
        List<User> result = userService.getAll();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(list, result);
        Mockito.verify(repository, times(1)).getAll();
    }
}