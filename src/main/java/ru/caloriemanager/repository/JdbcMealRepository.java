package ru.caloriemanager.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.caloriemanager.model.Meal;
import ru.caloriemanager.model.User;

import javax.sql.rowset.JdbcRowSet;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public class JdbcMealRepository implements MealRepository{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    @Override
    public int getCountUsers() {
        return 0;
    }

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
        List<Meal> meals;
        meals = jdbcTemplate.query("SELECT * FROM users_meals WHERE user_id = ?", ROW_MAPPER, userId);
        return meals;
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return null;
    }
}
