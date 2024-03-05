package ru.caloriesmanager.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import ru.caloriesmanager.entity.User;
import ru.caloriesmanager.repository.UserRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Repository
public class JpaUserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public User save(User user) {
        try {
            if (user.isNew()) entityManager.persist(user);
            else entityManager.merge(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        Query query = entityManager.createQuery("DELETE FROM User WHERE id=:id");
        query.setParameter("id", id);
        try {
            int result = query.executeUpdate();
            return result != 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public User get(int id) {
        try {
            User user = entityManager.find(User.class, id);
            return user;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public User getByEmail(String email) {
        try {
            List<User> users = entityManager.createQuery("FROM User WHERE email=:email", User.class)
                    .setParameter("email", email)
                    .getResultList();
            return (users.isEmpty()) ? null : users.get(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public List<User> getAll() {
        try {
            List<User> users = entityManager.createQuery("FROM User", User.class)
                    .getResultList();
            return users.stream()
                    .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
