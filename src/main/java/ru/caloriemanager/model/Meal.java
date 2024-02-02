package ru.caloriemanager.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.Objects;


@Getter
@Setter
@NamedEntityGraphs({
        @NamedEntityGraph(name = "meal-user-entity-graph",
                attributeNodes = {@NamedAttributeNode("user")})
})
@Entity
@Table(name = "meals", indexes = @Index(columnList = "user_id, date_time"))
//@Table(name = "meals")
public class Meal extends AbstractBaseEntity {

    @Column(name="date_time")
    private LocalDateTime dateTime;

    @Column(name = "description")
    private String description;

    @Column(name = "calories")
    private int calories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Meal() {
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

    @PrePersist
    private void prePersist() {
        if (this.dateTime == null) {
            this.dateTime = LocalDateTime.now();
        }
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
}
