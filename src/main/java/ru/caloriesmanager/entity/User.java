package ru.caloriesmanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.caloriesmanager.service.CustomUserDetailsService;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.hibernate.annotations.Cache;


@Getter
@Setter
@Entity
@Table(name = "users", indexes = @Index(columnList = "email"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Cacheable
public class User extends AbstractNamedEntity {

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private boolean enabled;

    @CreationTimestamp
    @Column(name = "registered")
    private LocalDateTime registered;

    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private Set<Role> roles;

    @Column(name = "calories_per_day")
    private int caloriesPerDay;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Meal> meals;

    public User() {
        super(null, null);
    }

    public User(Integer id, String name, String email, String password, Role role, Role... roles) {
        this(id, name, email, password, CustomUserDetailsService.DEFAULT_CALORIES_PER_DAY, true, EnumSet.of(role, roles));
    }

    public User(Integer id, String name, String email, String password, int caloriesPerDay, boolean enabled, Set<Role> roles) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.caloriesPerDay = caloriesPerDay;
        this.enabled = enabled;
        this.roles = roles;
    }

    @PreUpdate
    @PrePersist
    private void prePersist() {
        if (this.registered == null)
            this.registered = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "User (" +
                "id=" + id +
                ", email=" + email +
                ", name=" + name +
                ", enabled=" + enabled +
                ", roles=" + roles +
                ", caloriesPerDay=" + caloriesPerDay +
                ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email)
                && Objects.equals(id, user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, id);
    }
}