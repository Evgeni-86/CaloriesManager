package ru.caloriesmanager.repository.dataJpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.caloriesmanager.model.User;

public interface UserProxy extends JpaRepository<User, Integer> {
}
