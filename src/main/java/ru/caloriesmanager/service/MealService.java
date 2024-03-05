package ru.caloriesmanager.service;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.caloriesmanager.entity.Meal;
import ru.caloriesmanager.repository.MealRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import static ru.caloriesmanager.util.ValidationUtil.*;


@Service
public class MealService {
    @Autowired
    private MealRepository repository;
    @Autowired
    private CacheManager cacheManager;
    private static final Logger LOG_MEAL_SERVICE = LoggerFactory.getLogger(MealService.class);

    @Caching(evict = {
            @CacheEvict(value = "mealCache", key = "#p1"),
            @CacheEvict(value = "mealCache", key = "#p0.id + '|' +#p1")
    })
    @CacheEvict(value = "betweenCacheKeys", key = "#p1")
    public Meal create(@NonNull Meal meal, @Min(1) int userId) {
        checkNew(meal);
        return repository.save(meal, userId);
    }

    @Caching(evict = {
            @CacheEvict(value = "mealCache", key = "#p1"),
            @CacheEvict(value = "mealCache", key = "#p0.id + '|' +#p1")
    })
    @CacheEvict(value = "betweenCacheKeys", key = "#p1")
    public void update(@NonNull Meal meal, @Min(1) int userId) {
        checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }

    @Caching(evict = {
            @CacheEvict(value = "mealCache", key = "#p1"),
            @CacheEvict(value = "mealCache", key = "#p0 + '|' +#p1")
    })
    @CacheEvict(value = "betweenCacheKeys", key = "#p1")
    public boolean delete(@Min(1) int id, @Min(1) int userId) {
        checkNotFoundWithId(repository.get(id, userId), id);
        return repository.delete(id, userId);
    }

    @Cacheable(value = "mealCache", key = "#p0 + '|' + #p1")
    public Meal get(@Min(1) int id, @Min(1) int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    @Cacheable(value = "mealCache", key = "#p0")
    public List<Meal> getAll(@Min(1) int userId) {
        return repository.getAll(userId);
    }

    public List<Meal> getBetweenDates(@NotNull LocalDate startDate, @NotNull LocalDate endDate, @Min(1) int userId) {

        Cache betweenCacheKeys = cacheManager.getCache("betweenCacheKeys");
        Cache betweenListCache = cacheManager.getCache("betweenListCache");
        boolean cachesAvailable = betweenCacheKeys != null && betweenListCache != null;
        SimpleKey inputKey = new SimpleKey(startDate, endDate, userId);

//        Cache.ValueWrapper valueWrapper;
//        if (cachesAvailable && (valueWrapper = betweenCacheKeys.get(userId)) != null) {
//            SimpleKey listCacheKey = (SimpleKey) valueWrapper.get();
//            if (inputKey.equals(listCacheKey)) {
//                LOG_MEAL_SERVICE.info("return from cache");
//                return (List<Meal>) betweenListCache.get(listCacheKey).get();
//            } else if (listCacheKey != null) {
//                betweenCacheKeys.evictIfPresent(userId);
//                betweenListCache.evictIfPresent(listCacheKey);
//            }
//        }

        if (cachesAvailable) {
            SimpleKey listCacheKey = betweenCacheKeys.get(userId, SimpleKey.class);
            if (listCacheKey != null) {
                if (listCacheKey.equals(inputKey)) {
                    LOG_MEAL_SERVICE.info("return from cache");
                    return (List<Meal>) betweenListCache.get(listCacheKey).get();
                } else {
                    betweenCacheKeys.evictIfPresent(userId);
                    betweenListCache.evictIfPresent(listCacheKey);
                }
            }
        }

        List<Meal> mealList = repository.getBetween(
                LocalDateTime.of(startDate, LocalTime.MIN),
                LocalDateTime.of(endDate, LocalTime.MAX), userId);

        if (cachesAvailable) {
            betweenCacheKeys.put(userId, inputKey);
            betweenListCache.put(inputKey, mealList);
        }

        LOG_MEAL_SERVICE.info("new request to database");
        return mealList;
    }
}