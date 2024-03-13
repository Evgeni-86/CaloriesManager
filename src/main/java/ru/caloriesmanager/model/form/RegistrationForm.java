package ru.caloriesmanager.model.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.caloriesmanager.entity.Role;
import ru.caloriesmanager.entity.User;


@NoArgsConstructor
@Getter
@Setter
public class RegistrationForm {
    @NotBlank(message = "Name cannot be blank")
    @Pattern(regexp = "^(?!\\s)(.*\\S)$", message = "Name cannot start and end with whitespace")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    public static User getUserInstance(RegistrationForm registrationForm){
        return new User(
                null,
                registrationForm.getName(),
                registrationForm.getEmail(),
                registrationForm.getPassword(),
                Role.ROLE_USER);
    }
}
