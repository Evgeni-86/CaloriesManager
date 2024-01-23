package ru.caloriemanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.caloriemanager.model.Meal;
import ru.caloriemanager.repository.MealRepository;
import ru.caloriemanager.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.caloriemanager.util.ValidationUtil.*;

@Service
public class MealService {
    @Qualifier("inMemoryMealRepo")
    @Autowired
    private MealRepository repository;

    public Meal create(Meal meal, int userId) {
        if (meal == null || userId < 1) throw new IllegalArgumentException("not valid arguments");
        checkNew(meal);
        return repository.save(meal, userId);
    }

    public void update(Meal meal, int userId) {
        if (meal == null || userId < 1) throw new IllegalArgumentException("not valid arguments");
        checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }

    public boolean delete(int id, int userId) {
        if (id < 1 || userId < 1) throw new IllegalArgumentException("not valid arguments");
        checkNotFoundWithId(repository.get(id, userId), id);
        return repository.delete(id, userId);
    }

    public Meal get(int id, int userId) {
        if (id < 1 || userId < 1) throw new IllegalArgumentException("not valid arguments");
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    public List<Meal> getAll(int userId) {
        if (userId < 1) throw new IllegalArgumentException("not valid arguments");
        return repository.getAll(userId);
    }

    public List<Meal> getBetweenDates(@Nullable LocalDate startDate, @Nullable LocalDate endDate, int userId) {
        if (userId < 1) throw new IllegalArgumentException("not valid arguments");
        return repository.getBetween(
                DateTimeUtil.createDateTime(startDate, LocalDate.MIN, LocalTime.MIN),
                DateTimeUtil.createDateTime(endDate, LocalDate.MAX, LocalTime.MAX), userId);
    }

    public List<Meal> getBetweenDatesTimes(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        if (userId < 1) throw new IllegalArgumentException("not valid arguments");
        if (startDateTime == null || endDateTime == null) throw new IllegalArgumentException("not valid arguments");
        return repository.getBetween(startDateTime, endDateTime, userId);
    }
}