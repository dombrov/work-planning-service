package org.planning.server.api.v1.dto;

import org.planning.model.Worker;

import java.util.Objects;

public class WorkerDto {
    public String id;
    public String firstName;
    public String lastName;
    public boolean active;

    public static WorkerDto of(Worker worker) {
        WorkerDto dto = new WorkerDto();
        dto.id = worker.getId();
        dto.firstName = worker.getFirstName();
        dto.lastName = worker.getLastName();
        dto.active = worker.isActive();
        return dto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkerDto workerDto = (WorkerDto) o;
        return active == workerDto.active && Objects.equals(id, workerDto.id) && Objects.equals(firstName, workerDto.firstName) && Objects.equals(lastName, workerDto.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, active);
    }
}
