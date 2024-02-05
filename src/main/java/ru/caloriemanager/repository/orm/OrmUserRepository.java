package ru.caloriemanager.repository.orm;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.caloriemanager.model.User;
import ru.caloriemanager.repository.UserRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrmUserRepository implements UserRepository {

    private static final Logger LOG = LoggerFactory.getLogger(OrmUserRepository.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public User save(User user) {
        String mes = user.isNew() ? "save" : "update";
        LOG.info("trying {} in database user {}", mes, user);
        try {
            if (user.isNew()) entityManager.persist(user);
            else entityManager.merge(user);
        } catch (Exception e) {
            LOG.error("Error {} user {}", mes, user);
            throw new RuntimeException(String.format("Error %s user : %s", mes, e.getMessage()));
        }
        LOG.info("{} in database user {}", mes, user);
        return user;
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        LOG.info("trying delete from database user id = {}", id);
        Query query = entityManager.createQuery("DELETE FROM User WHERE id=:id");
        query.setParameter("id", id);
        try {
            int result = query.executeUpdate();
            if (result != 0) LOG.info("delete from database user id = {}", id);
            return result != 0;
        } catch (Exception e) {
            LOG.error("Error delete user id = {}", id);
            throw new RuntimeException(String.format("Error delete user : %s", e.getMessage()));
        }
    }

    @Transactional
    @Override
    public User get(int id) {
        LOG.info("trying to get user id = {}", id);
        try {
            User user = entityManager.find(User.class, id);
            if (user != null) LOG.info("get user id = {}", id);
            return user;
        } catch (Exception e) {
            LOG.error("Error get user id = {}", id);
            throw new RuntimeException(String.format("Error get user : %s", e.getMessage()));
        }
    }

    @Transactional
    @Override
    public User getByEmail(String email) {
        LOG.info("trying to get user by email = {}", email);
        try {
            List<User> users = entityManager.createQuery("FROM User WHERE email=:email", User.class)
                    .setParameter("email", email)
                    .getResultList();
            if (!users.isEmpty())
                LOG.info("get user by email = {}", email);
            return (users.isEmpty()) ? null : users.get(0);
        } catch (Exception e) {
            LOG.error("Error get user by email = {}", email);
            throw new RuntimeException(String.format("Error get user by email : %s", e.getMessage()));
        }
    }

    @Transactional
    @Override
    public List<User> getAll() {
        LOG.info("trying to get all users");
        try {
            List<User> users = entityManager.createQuery("FROM User", User.class)
                    .getResultList();
            LOG.info("get all users");
            return users.stream()
                    .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception e) {
            LOG.error("Error get all users");
            throw new RuntimeException(String.format("Error get all users : %s", e.getMessage()));
        }
    }
}
