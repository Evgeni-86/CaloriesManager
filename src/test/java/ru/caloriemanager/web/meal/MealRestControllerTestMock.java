package ru.caloriemanager.web.meal;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.caloriemanager.model.*;
import ru.caloriemanager.repository.MealRepository;
import ru.caloriemanager.repository.mock.InMemoryMealRepository;
import ru.caloriemanager.service.MealService;
import ru.caloriemanager.util.MealsUtil;
import ru.caloriemanager.web.SecurityUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MealRestControllerTestMock {

    @Mock
    private MealService mealService;
    @InjectMocks
    private MealRestController mealRestController;
    private MockMvc mockMvc;
    private static int userID;
    private static int userCalories;
    private static final MealRepository mealRepository = new InMemoryMealRepository();


    @BeforeAll
    static void init() {
        SecurityUtil.setUserId(10);
        userID = 10;
        userCalories = SecurityUtil.authUserCaloriesPerDay();
    }

//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(mealRestController).build();
//    }

    @Test
    void create() {
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        mealRestController.create(meal);
        Mockito.verify(mealService).create(meal, userID);
    }

    @Test
    void update() {
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        Meal mealUpdate = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "ЗавтракUpdate", 500);
        mealRestController.create(meal);
        mealRestController.update(mealUpdate);
        Mockito.verify(mealService).update(mealUpdate, userID);
    }

    @Test
    void get() {
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        mealRestController.create(meal);
        int mealId = (meal.getId() == null) ? 1 : meal.getId();
        mealRestController.get(mealId);
        Mockito.verify(mealService).get(mealId, userID);
    }

    @Test
    void delete() {
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,
                30, 13, 0), "Завтрак", 500);
        mealRestController.create(meal);
        int mealId = (meal.getId() == null) ? 1 : meal.getId();
        mealRestController.delete(mealId);
        Mockito.verify(mealService).delete(mealId, userID);
    }

    @Test
    void getAll() {
        try (MockedStatic<MealsUtil> mockedStatic = mockStatic(MealsUtil.class)) {
            mealRestController.getAll();
            Mockito.verify(mealService).getAll(userID);
            mockedStatic.verify(
                    () -> MealsUtil.getFilteredTos(
                            mealService.getAll(userID),
                            userCalories, LocalTime.MIN, LocalTime.MAX)
            );
        }
    }

    @Test
    void getBetween() {

    }
}