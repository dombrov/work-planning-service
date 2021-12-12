package org.planning.server.api.dto;

import org.planning.model.Shift;

import java.time.Instant;

public class ShiftDto {

    public Long id;
    public String workerId;
    public Instant startTime;
    public Instant endTime;

    public static ShiftDto of(Shift shift) {
        ShiftDto dto = new ShiftDto();
        dto.id = shift.getId();
        dto.workerId = shift.getWorkerId();
        dto.startTime = shift.getStartTime();
        dto.endTime = shift.getEndTime();
        return dto;
    }

}
