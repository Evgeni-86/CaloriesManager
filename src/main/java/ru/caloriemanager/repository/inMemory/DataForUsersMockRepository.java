package ru.caloriemanager.repository.inMemory;

import ru.caloriemanager.model.Role;
import ru.caloriemanager.model.User;

import java.util.ArrayList;
import java.util.List;

public class DataForUsersMockRepository {
    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;
    public static final int COUNT_USERS = 2;
    public static List<User> USERS_LIST = new ArrayList<>(){{
        add(new User(null, "User_1", "user_email@mail.ru", "user_pass", Role.ROLE_USER));
        add(new User(null, "Admin", "admin_email@mail.ru", "admin_pass", Role.ROLE_ADMIN));
    }};
}
