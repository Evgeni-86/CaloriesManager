package ru.caloriesmanager.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import ru.caloriesmanager.entity.Meal;
import ru.caloriesmanager.entity.User;
import ru.caloriesmanager.repository.MealRepository;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public class JpaMealRepositoryImpl implements MealRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public Meal save(Meal meal, int userId) {
        try {
            User user = entityManager.getReference(User.class, userId);
            meal.setUser(user);
            if (meal.isNew()) entityManager.persist(meal);
            else entityManager.merge(meal);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return meal;
    }

    @Transactional
    @Override
    public boolean delete(int id, int userId) {
        Query query = entityManager.createQuery("DELETE FROM Meal WHERE id=:id");
        query.setParameter("id", id);
        try {
            int result = query.executeUpdate();
            return result != 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Meal get(int id, int userId) {
        try {
            return entityManager.find(Meal.class, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public List<Meal> getAll(int userId) {
        try {
            List<Meal> meals = entityManager.createQuery("FROM Meal WHERE user.id=:id ORDER BY dateTime DESC", Meal.class)
                    .setParameter("id", userId)
                    .getResultList();
            return meals;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public List<Meal> getBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        try {
            List<Meal> meals = entityManager.createQuery("FROM Meal WHERE user.id=:id " +
                            "AND dateTime BETWEEN :sdt AND :edt ORDER BY dateTime DESC", Meal.class)
                    .setParameter("id", userId)
                    .setParameter("sdt", startDateTime)
                    .setParameter("edt", endDateTime)
                    .getResultList();
            return meals;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
