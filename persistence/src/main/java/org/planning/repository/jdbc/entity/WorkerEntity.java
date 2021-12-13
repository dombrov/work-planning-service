package org.planning.repository.jdbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table("workers")
public class WorkerEntity {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private boolean active;

    public WorkerEntity() {
    }

    public WorkerEntity(String id, String firstName, String lastName, boolean active) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkerEntity that = (WorkerEntity) o;
        return active == that.active && Objects.equals(id, that.id) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, active);
    }
}
