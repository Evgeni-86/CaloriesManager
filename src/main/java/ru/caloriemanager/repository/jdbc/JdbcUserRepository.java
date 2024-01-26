package ru.caloriemanager.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.caloriemanager.model.Meal;
import ru.caloriemanager.model.User;
import ru.caloriemanager.repository.UserRepository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    public JdbcUserRepository(@Autowired @Qualifier("dataSource") DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public int getCountUser() {
        return 0;
    }

    @Override
    public User save(User user) {
        Number insertKey = null;
        int countUpdateRaw = 0;

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource
                .addValue("id", user.getId())
                .addValue("name", user.getName())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("registered", user.getRegistered())
                .addValue("enabled", user.isEnabled())
                .addValue("calories_per_day", user.getCaloriesPerDay());

        if (user.isNew()) {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            simpleJdbcInsert.setTableName("users");
            simpleJdbcInsert.usingGeneratedKeyColumns("id");
            insertKey = simpleJdbcInsert.executeAndReturnKey(mapSqlParameterSource);
            user.setId(insertKey.intValue());
        } else {
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
            String sql = "UPDATE users SET name=:name, email=:email, password=:password, " +
                    "registered=:registered, enabled=:enabled, calories_per_day=:calories_per_day WHERE id=:id";
            countUpdateRaw = namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
        }

        if (insertKey != null || countUpdateRaw > 0) return user;
        else throw new RuntimeException("error write to database");
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        int countDeleteRaw = jdbcTemplate.update(sql, id);
        return countDeleteRaw > 0;
    }

    @Override
    public User get(int id) {
        User user;
        user = jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?", ROW_MAPPER, id);
        if (user != null)
            return user;
        else throw new RuntimeException("error read from database");
    }
//    @Override
//    public Meal get(int id, int userId) {
//        List<Meal> listRowSet = jdbcTemplate.query("SELECT * FROM meals WHERE user_id = ? AND id = ?", ROW_MAPPER, userId, id);
//        if (!listRowSet.isEmpty())
//            return listRowSet.get(0);
//        else throw new RuntimeException("error read from database");
//    }


    @Override
    public User getByEmail(String email) {
        User user;
        user = jdbcTemplate.queryForObject("SELECT * FROM users WHERE email = ?", ROW_MAPPER, email);
        if (user != null)
            return user;
        else throw new RuntimeException("error read from database");
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users", ROW_MAPPER);
        if (!users.isEmpty())
            return users;
        else throw new RuntimeException("error read from database");
    }
}
