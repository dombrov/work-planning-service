package org.planning.model;

import java.util.Objects;

public class Worker {

    private final String id;
    private final String firstName;
    private final String lastName;
    private final boolean active;

    public Worker(String id, String firstName, String lastName, boolean active) {
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
        Worker worker = (Worker) o;
        return id == worker.id && active == worker.active && Objects.equals(firstName, worker.firstName) && Objects.equals(lastName, worker.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, active);
    }

    @Override
    public String toString() {
        return "Worker{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", active=" + active +
                '}';
    }
}
