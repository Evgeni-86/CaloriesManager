package ru.caloriesmanager.repository.dataJpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.caloriesmanager.model.Meal;

public interface MealProxy extends JpaRepository<Meal, Integer> {
}
