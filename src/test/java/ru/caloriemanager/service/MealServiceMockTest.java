package ru.caloriemanager.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.caloriemanager.entity.Meal;
import ru.caloriemanager.repository.MealRepository;
import ru.caloriemanager.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;


//@ExtendWith(SpringExtension.class)
//@ContextConfiguration("classpath:spring/spring-app.xml")
@ExtendWith(MockitoExtension.class)
class MealServiceMockTest {
    @Mock
    private MealRepository repository;
    @InjectMocks
    private MealService mealService;

    private static final int USER_ID = 1;
    private static final Meal MEAL_WITH_ID = new Meal(1, LocalDateTime.now(), "meal test with id", 1000);
    private static final Meal MEAL_WITHOUT_ID = new Meal(LocalDateTime.now(), "meal test", 1000);
    private static final LocalDateTime START_DATE_TIME = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN);
    private static final LocalDateTime END_DATE_TIME = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    private static final List<Meal> TEST_LIST = new ArrayList<>() {{
        add(new Meal(1, LocalDateTime.now(), "meal test", 1000));
    }};

    @Test
    void create() {
        Mockito.when(repository.save(MEAL_WITHOUT_ID, USER_ID)).thenReturn(MEAL_WITHOUT_ID);
        Meal result = mealService.create(MEAL_WITHOUT_ID, USER_ID);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(MEAL_WITHOUT_ID, result);
        Mockito.verify(repository, times(1)).save(MEAL_WITHOUT_ID, USER_ID);
    }

    @Test
    void createIllegalArgumentException() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> mealService.create(MEAL_WITH_ID, USER_ID));
        Assertions.assertEquals(MEAL_WITH_ID + " must be new (id=null)", exception.getMessage());
        Mockito.verify(repository, times(0)).save(MEAL_WITH_ID, USER_ID);
    }

    @Test
    void update() {
        Mockito.when(repository.save(MEAL_WITH_ID, USER_ID)).thenReturn(MEAL_WITH_ID);
        mealService.update(MEAL_WITH_ID, USER_ID);
        Mockito.verify(repository, times(1)).save(MEAL_WITH_ID, USER_ID);
    }

    @Test
    void updateNotFoundException() {
        Mockito.when(repository.save(MEAL_WITH_ID, USER_ID)).thenReturn(null);
        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> mealService.update(MEAL_WITH_ID, USER_ID));
        Assertions.assertEquals("Not found entity with id=" + MEAL_WITH_ID.getId(), exception.getMessage());
        Mockito.verify(repository, times(1)).save(MEAL_WITH_ID, USER_ID);
    }

    @Test
    void delete() {
        Mockito.when(repository.get(MEAL_WITH_ID.getId(), USER_ID)).thenReturn(MEAL_WITH_ID);
        Mockito.when(repository.delete(MEAL_WITH_ID.getId(), USER_ID)).thenReturn(true);
        Assertions.assertTrue(mealService.delete(MEAL_WITH_ID.getId(), USER_ID));
        Mockito.verify(repository, times(1)).get(MEAL_WITH_ID.getId(), USER_ID);
        Mockito.verify(repository, times(1)).delete(MEAL_WITH_ID.getId(), USER_ID);
    }

    @Test
    void deleteNotFoundException() {
        Mockito.when(repository.get(1, USER_ID)).thenReturn(null);
        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> mealService.delete(MEAL_WITH_ID.getId(), USER_ID));
        Assertions.assertEquals("Not found entity with id=" + MEAL_WITH_ID.getId(), exception.getMessage());
        Mockito.verify(repository, times(1)).get(MEAL_WITH_ID.getId(), USER_ID);
        Mockito.verify(repository, times(0)).delete(MEAL_WITH_ID.getId(), USER_ID);
    }

    @Test
    void get() {
        Mockito.when(repository.get(MEAL_WITH_ID.getId(), USER_ID)).thenReturn(MEAL_WITH_ID);
        Meal result = mealService.get(MEAL_WITH_ID.getId(), USER_ID);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(MEAL_WITH_ID, result);
        Mockito.verify(repository, times(1)).get(MEAL_WITH_ID.getId(), USER_ID);
    }

    @Test
    void getNotFoundException() {
        Mockito.when(repository.get(1, USER_ID)).thenReturn(null);
        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> mealService.get(MEAL_WITH_ID.getId(), USER_ID));
        Assertions.assertEquals("Not found entity with id=" + MEAL_WITH_ID.getId(), exception.getMessage());
        Mockito.verify(repository, times(1)).get(MEAL_WITH_ID.getId(), USER_ID);
    }

    @Test
    void getAll() {
        Mockito.when(repository.getAll(USER_ID)).thenReturn(TEST_LIST);
        List<Meal> result = mealService.getAll(USER_ID);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(TEST_LIST, result);
        Mockito.verify(repository, times(1)).getAll(USER_ID);
    }

    @Test
    void getBetweenDates() {
        Mockito.when(repository.getBetween(START_DATE_TIME, END_DATE_TIME, USER_ID)).thenReturn(TEST_LIST);
        List<Meal> result = mealService.getBetweenDates(START_DATE_TIME.toLocalDate(), END_DATE_TIME.toLocalDate(), USER_ID);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(TEST_LIST, result);
        Mockito.verify(repository, times(1)).getBetween(START_DATE_TIME, END_DATE_TIME, USER_ID);
    }

    @Test
    void getBetweenDatesTimes() {
        Mockito.when(repository.getBetween(START_DATE_TIME, END_DATE_TIME, USER_ID)).thenReturn(TEST_LIST);
        List<Meal> result = mealService.getBetweenDatesTimes(START_DATE_TIME, END_DATE_TIME, USER_ID);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(TEST_LIST, result);
        Mockito.verify(repository, times(1)).getBetween(START_DATE_TIME, END_DATE_TIME, USER_ID);
    }
}