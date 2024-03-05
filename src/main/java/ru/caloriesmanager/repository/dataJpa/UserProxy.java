package ru.caloriesmanager.repository.dataJpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.caloriesmanager.entity.User;

public interface UserProxy extends JpaRepository<User, Integer> {
}
