package ru.caloriemanager.repository.orm;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.caloriemanager.model.Meal;
import ru.caloriemanager.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public class OrmMealRepository implements MealRepository {
    private static final Logger LOG = LoggerFactory.getLogger(OrmMealRepository.class);
    @PersistenceContext
    public EntityManager entityManager;

    @Transactional
    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            entityManager.persist(meal);
            LOG.info("save in database new meal {}", meal);
        } else {
            entityManager.merge(meal);
            LOG.info("update in database meal {}", meal);
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        return false;
    }

    @Transactional
    @Override
    public Meal get(int id, int userId) {
        return entityManager.find(Meal.class, id);
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
