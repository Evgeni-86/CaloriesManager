package ru.caloriesmanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.caloriesmanager.web.SecurityUtil;
import java.time.*;
import java.util.Objects;
import org.hibernate.annotations.Cache;


@Getter
@Setter
@Entity
@Table(name = "meals", indexes = @Index(columnList = "user_id, date_time"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Cacheable
public class Meal extends AbstractBaseEntity {

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "description")
    private String description;

    @Column(name = "calories")
    private int calories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Meal() {
        super(null);
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    @PreUpdate
    @PrePersist
    private void prePersist() {
        if (this.dateTime == null)
            this.dateTime = LocalDateTime.now();
    }

    @PostLoad
    private void postLoadEntity() {
        ZonedDateTime systemZoned = ZonedDateTime.of(dateTime, ZoneId.systemDefault());
        ZonedDateTime userZoned = systemZoned.withZoneSameInstant(SecurityUtil.zoneId);
        dateTime = userZoned.toLocalDateTime();
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return Objects.equals(id, meal.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
