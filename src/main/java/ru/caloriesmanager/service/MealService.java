package ru.caloriesmanager.service;

import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.caloriesmanager.model.Meal;
import ru.caloriesmanager.repository.MealRepository;
import ru.caloriesmanager.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.caloriesmanager.util.ValidationUtil.*;


@Service
public class MealService {
    @Autowired
    private MealRepository repository;

    public Meal create(@NonNull Meal meal, @Min(1) int userId) {
        checkNew(meal);
        return repository.save(meal, userId);
    }

    public void update(@NonNull Meal meal, @Min(1) int userId) {
        checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }

    public boolean delete(@Min(1) int id, @Min(1) int userId) {
        checkNotFoundWithId(repository.get(id, userId), id);
        return repository.delete(id, userId);
    }

    public Meal get(@Min(1) int id, @Min(1) int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    public List<Meal> getAll(@Min(1) int userId) {
        return repository.getAll(userId);
    }

    public List<Meal> getBetweenDates(@Nullable LocalDate startDate, @Nullable LocalDate endDate, @Min(1) int userId) {
        return repository.getBetween(
                DateTimeUtil.createDateTime(startDate, DateTimeUtil.MIN_DATE, LocalTime.MIN),
                DateTimeUtil.createDateTime(endDate, DateTimeUtil.MAX_DATE, LocalTime.MAX), userId);
    }

    public List<Meal> getBetweenDatesTimes(@NonNull LocalDateTime startDateTime, @NonNull LocalDateTime endDateTime, @Min(1) int userId) {
        return repository.getBetween(startDateTime, endDateTime, userId);
    }
}