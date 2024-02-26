package ru.caloriesmanager.model;

import lombok.Getter;
import ru.caloriesmanager.entity.Role;
import ru.caloriesmanager.entity.User;
import java.time.LocalDateTime;
import java.util.Set;


@Getter
public class UserViewModel {

    private final String name;
    private final String email;
    private final String password;
    private final boolean enabled;
    private final LocalDateTime registered;
    private final int caloriesPerDay;
    private final Set<Role> roles;

    private UserViewModel(String name, String email, String password, boolean enabled, LocalDateTime registered, int caloriesPerDay, Set<Role> roles) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.registered = registered;
        this.caloriesPerDay = caloriesPerDay;
        this.roles = roles;
    }

    public static UserViewModel getModel(User user){
        return new UserViewModel(
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                user.getRegistered(),
                user.getCaloriesPerDay(),
                user.getRoles());
    }

    @Override
    public String toString() {
        return "UserViewModel{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", registered=" + registered +
                ", caloriesPerDay=" + caloriesPerDay +
                ", roles=" + roles +
                '}';
    }
}
