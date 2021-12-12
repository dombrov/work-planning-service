package org.planning.server.api.dto;

import org.planning.model.Worker;

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

}
