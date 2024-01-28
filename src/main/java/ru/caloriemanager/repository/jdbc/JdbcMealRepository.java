package ru.caloriemanager.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.caloriemanager.model.Meal;
import ru.caloriemanager.repository.MealRepository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public class JdbcMealRepository implements MealRepository {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcMealRepository.class);
    private final JdbcTemplate jdbcTemplate;
    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    public JdbcMealRepository(@Autowired @Qualifier("dataSource") DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Number insertKey = null;
        int countUpdateRaw = 0;

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource
                .addValue("id", meal.getId())
                .addValue("calories", meal.getCalories())
                .addValue("description", meal.getDescription())
                .addValue("user_id", userId);

        if (meal.isNew()) {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            simpleJdbcInsert.setTableName("meals");
            simpleJdbcInsert.usingColumns("calories", "description", "user_id");
            simpleJdbcInsert.usingGeneratedKeyColumns("id");
            insertKey = simpleJdbcInsert.executeAndReturnKey(mapSqlParameterSource);
            meal.setId(insertKey.intValue());
            LOG.info("save in database new meal {}", meal);
        } else {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
            String sql = "UPDATE meals SET calories =:calories, date_time = current_timestamp, description =:description, " +
                    "user_id =:user_id WHERE id=:id";
            countUpdateRaw = namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
            LOG.info("update in database meal {}", meal);
        }

        if (insertKey != null || countUpdateRaw > 0) return meal;
        else throw new RuntimeException("error write to database");
    }

    @Override
    public boolean delete(int id, int userId) {
        String sql = "DELETE FROM meals WHERE user_id = ? AND id = ?";
        int countDeleteRaw = jdbcTemplate.update(sql, userId, id);
        return countDeleteRaw > 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> listRowSet = jdbcTemplate.query("SELECT * FROM meals WHERE user_id = ? AND id = ?", ROW_MAPPER, userId, id);
        if (!listRowSet.isEmpty())
            return listRowSet.get(0);
        else return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        List<Meal> meals = jdbcTemplate.query("SELECT * FROM meals WHERE user_id = ?", ROW_MAPPER, userId);
        if (!meals.isEmpty())
            return meals;
        else throw new RuntimeException("error read from database");
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        String sql = "SELECT * FROM meals WHERE user_id = ? AND date_time BETWEEN ? and ?";
        List<Meal> meals = jdbcTemplate.query(sql, ROW_MAPPER, userId, startDateTime, endDateTime);
        if (!meals.isEmpty())
            return meals;
        else throw new RuntimeException("error read from database");
    }
}
