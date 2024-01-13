package ru.caloriemanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.caloriemanager.model.Meal;
import ru.caloriemanager.repository.MealRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.caloriemanager.util.ValidationUtil.*;

@Service
public class MealService {
    @Autowired
    private MealRepository repository;

    public Meal create(Meal meal, int userId) {
        checkNew(meal);
        return repository.save(meal, userId);
    }

    public void update(Meal meal, int userId) {
        checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }

    public boolean delete(int id, int userId) {
        checkNotFoundWithId(repository.get(id, userId), id);
        return repository.delete(id, userId);
    }

    public Meal get(int id, int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    public List<Meal> getBetweenDates(LocalDate startDate, LocalDate endDate, int userId) {
        return getBetweenDatesTimes(LocalDateTime.of(startDate, LocalTime.MIN), LocalDateTime.of(endDate, LocalTime.MAX), userId);
    }

    public List<Meal> getBetweenDatesTimes(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return repository.getBetween(startDateTime, endDateTime, userId);
    }
}