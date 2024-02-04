package ru.caloriemanager.model;

import jakarta.persistence.*;

import java.util.Objects;

@MappedSuperclass
public abstract class AbstractBaseEntity {

    public static final int START_SEQ = 100;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    protected Integer id;


    protected AbstractBaseEntity(Integer id) {
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public boolean isNew() {
        return this.id == null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ":" + id;
    }
}