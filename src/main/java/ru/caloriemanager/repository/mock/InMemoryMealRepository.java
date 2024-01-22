package ru.caloriemanager.repository.mock;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import ru.caloriemanager.model.Meal;
import ru.caloriemanager.repository.MealRepository;
import ru.caloriemanager.service.MealService;
import ru.caloriemanager.util.DateTimeUtil;
import ru.caloriemanager.util.MealsUtil;
import ru.caloriemanager.web.SecurityUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository("inMemoryMealRepo")
public class InMemoryMealRepository extends InMemoryBaseRepository<Meal> implements MealRepository {

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private Map<Integer, Map<Integer, Meal>> usersMealsMap = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, InMemoryUserRepository.USER_ID));
        save(new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510),
                InMemoryUserRepository.ADMIN_ID);
        save(new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 21, 0), "Админ ужин", 1500),
                InMemoryUserRepository.ADMIN_ID);
    }

    public int getCountUsers() {
        return usersMealsMap.size();
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> meals = usersMealsMap.computeIfAbsent(userId, ConcurrentHashMap::new);
//        Map<Integer, Meal> meals2 = usersMealsMap.computeIfAbsent(userId, (user)-> new ConcurrentHashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meals.put(meal.getId(), meal);
            LOG.info("user {} create meal {}", SecurityUtil.authUserId(), meal);
            return meal;
        }
        LOG.info("user {} update meal {}", SecurityUtil.authUserId(), meal);
        return meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        LOG.info("user {} delete meal id = {}", SecurityUtil.authUserId(), id);
        Map<Integer, Meal> meals = usersMealsMap.get(userId);
        return meals != null && meals.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        LOG.info("user {} get meal id = {}", SecurityUtil.authUserId(), id);
        Map<Integer, Meal> meals = usersMealsMap.get(userId);
        return meals == null ? null : meals.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        LOG.info("getAll for user {}", SecurityUtil.authUserId());
        return getAllFiltered(userId, meal -> true);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        LOG.info("getBetween date_time({} - {}) for user {}", startDateTime, endDateTime, userId);
        return getAllFiltered(userId, meal -> DateTimeUtil.isBetweenInclusive(meal.getDateTime(), startDateTime, endDateTime));
    }

    private List<Meal> getAllFiltered(int userId, Predicate<Meal> filter) {
        Map<Integer, Meal> meals = usersMealsMap.get(userId);
        return CollectionUtils.isEmpty(meals) ? Collections.emptyList() :
                meals.values().stream()
                        .filter(filter)
                        .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(Collectors.toList());
    }

}
