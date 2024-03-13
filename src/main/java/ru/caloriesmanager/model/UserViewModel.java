package ru.caloriesmanager.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.caloriesmanager.entity.Role;
import ru.caloriesmanager.entity.User;
import java.time.LocalDateTime;
import java.util.Set;


@NoArgsConstructor
@Setter
@Getter
public class UserViewModel {

    private Integer id;
    @NotBlank(message = "{NotBlank.UserViewModel.name}")
    private String name;
    private String email;
    private String password;
    private boolean enabled;
    private LocalDateTime registered;
    @NotNull(message = "{NotNull.UserViewModel.calories}")
    private Integer caloriesPerDay;
    private Set<Role> roles;

    private UserViewModel(String name, String email, String password,
                          boolean enabled, LocalDateTime registered,
                          int caloriesPerDay, Set<Role> roles) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.registered = registered;
        this.caloriesPerDay = caloriesPerDay;
        this.roles = roles;
    }

    public static User getUserInstance(UserViewModel userViewModel) {
        return new User(
                userViewModel.getId(),
                userViewModel.getName(),
                userViewModel.getEmail(),
                userViewModel.getPassword(),
                Role.ROLE_USER);
    }

    public static UserViewModel getModel(User user) {
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
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", registered=" + registered +
                ", caloriesPerDay=" + caloriesPerDay +
                ", roles=" + roles +
                '}';
    }
}
