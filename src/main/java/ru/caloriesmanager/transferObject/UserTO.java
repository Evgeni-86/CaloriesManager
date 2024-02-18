package ru.caloriesmanager.transferObject;

import jakarta.persistence.*;
import lombok.Getter;
import ru.caloriesmanager.model.Role;
import ru.caloriesmanager.model.User;
import java.time.LocalDateTime;
import java.util.Set;


@Getter
public class UserTO {

    private final String name;
    private final String email;
    private final String password;
    private final boolean enabled;
    private final LocalDateTime registered;
    private final int caloriesPerDay;
    private final Set<Role> roles;

    private UserTO(String name, String email, String password, boolean enabled, LocalDateTime registered, int caloriesPerDay, Set<Role> roles) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.registered = registered;
        this.caloriesPerDay = caloriesPerDay;
        this.roles = roles;
    }

    public static UserTO getTransferObject(User user){
        return new UserTO(
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
        return "UserTO{" +
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
