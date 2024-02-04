package ru.caloriemanager.repository.orm;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Repository
public class OrmMealRepository implements MealRepository {
    private static final Logger LOG = LoggerFactory.getLogger(OrmMealRepository.class);
    @PersistenceContext
    public EntityManager entityManager;

    @Transactional
    @Override
    public Meal save(Meal meal, int userId) {
        String mes = meal.isNew() ? "save" : "update";
        LOG.info("trying {} in database meal {} user id = {}", mes, meal, userId);
        try {
            if (meal.isNew()) entityManager.persist(meal);
            else entityManager.merge(meal);
        } catch (Exception e) {
            LOG.error("Error {} meal {} user id = {}", mes, meal, userId);
            throw new RuntimeException(String.format("Error %s meal : %s", mes, e.getMessage()));
        }
        LOG.info("{} in database meal {} user id = {}", mes, meal, userId);
        return meal;
    }

    @Transactional
    @Override
    public boolean delete(int id, int userId) {
        LOG.info("trying delete from database meal id = {} user id = {}", id, userId);
        Query query = entityManager.createQuery("DELETE FROM Meal WHERE id=:id");
        query.setParameter("id", id);
        try {
            int result = query.executeUpdate();
            if (result != 0) LOG.info("delete from database meal id = {} user id = {}", id, userId);
            return result != 0;
        } catch (Exception e) {
            LOG.error("Error delete meal id = {} user id = {}", id, userId);
            throw new RuntimeException(String.format("Error delete meal : %s", e.getMessage()));
        }
    }

    @Transactional
    @Override
    public Meal get(int id, int userId) {
        LOG.info("trying to get meal id = {} user id = {}", id, userId);
        try {
            Meal meal = entityManager.find(
                    Meal.class, id,
                    Collections.singletonMap(
                            "jakarta.persistence.loadgraph",
                            entityManager.getEntityGraph("meal-user-entity-graph")
                    )
            );
            if (meal != null) LOG.info("get meal id = {} user id = {}", id, userId);
            return meal;
        } catch (Exception e) {
            LOG.error("Error get meal id = {} user id = {}", id, userId);
            throw new RuntimeException(String.format("Error get meal : %s", e.getMessage()));
        }
    }

    @Transactional
    public Meal getWithoutUser(int id, int userId) {
        LOG.info("trying to get meal id = {} user id = {}", id, userId);
        try {
            Meal meal = entityManager.find(Meal.class, id);
            if (meal != null) LOG.info("get meal id = {} user id = {}", id, userId);
            return meal;
        } catch (Exception e) {
            LOG.error("Error get meal id = {} user id = {}", id, userId);
            throw new RuntimeException(String.format("Error get meal : %s", e.getMessage()));
        }
    }

    @Transactional
    @Override
    public List<Meal> getAll(int userId) {
        LOG.info("trying to get all meals user id = {}", userId);
        Query query = entityManager.createQuery("FROM Meal WHERE user.id=:id ORDER BY dateTime DESC")
                .setParameter("id", userId)
                .setHint("jakarta.persistence.loadgraph", entityManager.getEntityGraph("meal-user-entity-graph"));
        try {
            List<Meal> meals = (List<Meal>) query.getResultList();
            LOG.info("get all meals user id = {}", userId);
            return meals;
        } catch (Exception e) {
            LOG.error("Error get all meals user id = {}", userId);
            throw new RuntimeException(String.format("Error get all meals : %s", e.getMessage()));
        }
    }

    @Transactional
    public List<Meal> getAllWithoutUser(int userId) {
        LOG.info("trying to get all meals user id = {}", userId);
        Query query = entityManager.createNativeQuery("SELECT * FROM meals WHERE user_id=:id", Meal.class);
        query.setParameter("id", userId);
        try {
            List<Meal> meals = (List<Meal>) query.getResultList();
            LOG.info("get all meals user id = {}", userId);
            return meals;
        } catch (Exception e) {
            LOG.error("Error get all meals user id = {}", userId);
            throw new RuntimeException(String.format("Error get all meals : %s", e.getMessage()));
        }
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        LOG.info("trying getBetween dateTime({} - {}) for user {}", startDateTime, endDateTime, userId);
        Query query = entityManager.createQuery("FROM Meal WHERE user.id=:id " +
                        "AND dateTime BETWEEN :sdt AND :edt ORDER BY dateTime DESC")
                .setParameter("id", userId)
                .setParameter("sdt", startDateTime)
                .setParameter("edt", endDateTime)
                .setHint("jakarta.persistence.loadgraph", entityManager.getEntityGraph("meal-user-entity-graph"));
        try {
            List<Meal> meals = (List<Meal>) query.getResultList();
            LOG.info("getBetween dateTime({} - {}) for user {}", startDateTime, endDateTime, userId);
            return meals;
        } catch (Exception e) {
            LOG.error("Error getBetween for user id = {}", userId);
            throw new RuntimeException(String.format("Error getBetween : %s", e.getMessage()));
        }
    }

}
