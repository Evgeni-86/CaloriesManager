package ru.caloriesmanager.repository.dataJpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.caloriesmanager.entity.User;
import ru.caloriesmanager.repository.UserRepository;

import java.util.List;

@Repository
public class DataJpaUserRepository implements UserRepository {

    @Autowired
    private UserProxy userProxy;

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public User get(int id) {
        return null;
    }

    @Override
    public User getByEmail(String email) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }
}
