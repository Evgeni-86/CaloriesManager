package ru.caloriesmanager.service;

import jakarta.validation.constraints.Min;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.caloriesmanager.entity.Meal;
import ru.caloriesmanager.repository.MealRepository;
import ru.caloriesmanager.util.DateTimeUtil;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.caloriesmanager.util.ValidationUtil.*;


@Service
public class MealService {
    @Autowired
    private MealRepository repository;

    @Caching(evict = {
            @CacheEvict(value = "MealCache", key = "#p0.id + '|' + #p1"),
            @CacheEvict(value = "MealGetAllCache", key = "#p1")
    })
    public Meal create(@NonNull Meal meal, @Min(1) int userId) {
        checkNew(meal);
        return repository.save(meal, userId);
    }

    @Caching(evict = {
            @CacheEvict(value = "MealCache", key = "#p0.id + '|' + #p1"),
            @CacheEvict(value = "MealGetAllCache", key = "#p1")
    })
    public void update(@NonNull Meal meal, @Min(1) int userId) {
        checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }

    @Caching(evict = {
            @CacheEvict(value = "MealCache", key = "#p0 + '|' + #p1"),
            @CacheEvict(value = "MealGetAllCache", key = "#p1")
    })
    public boolean delete(@Min(1) int id, @Min(1) int userId) {
        checkNotFoundWithId(repository.get(id, userId), id);
        return repository.delete(id, userId);
    }

//    @Cacheable(value = "MealCache")
    public Meal get(@Min(1) int id, @Min(1) int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    @Cacheable(value = "MealGetAllCache")
    public List<Meal> getAll(@Min(1) int userId) {
        return repository.getAll(userId);
    }

    public List<Meal> getBetweenDates(@Nullable LocalDate startDate, @Nullable LocalDate endDate, @Min(1) int userId) {
        return repository.getBetween(
                DateTimeUtil.createDateTime(startDate, DateTimeUtil.MIN_DATE, LocalTime.MIN),
                DateTimeUtil.createDateTime(endDate, DateTimeUtil.MAX_DATE, LocalTime.MAX), userId);
    }
}