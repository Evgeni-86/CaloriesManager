package ru.caloriesmanager.repository.dataJpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.caloriesmanager.entity.Meal;
import ru.caloriesmanager.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository{

    @Autowired
    private MealProxy mealProxy;

    @Override
    public Meal save(Meal meal, int userId) {
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return null;
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return null;
    }
}
