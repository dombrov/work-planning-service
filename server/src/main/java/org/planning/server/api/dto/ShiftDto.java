package org.planning.server.api.dto;

import org.planning.model.Shift;

import java.time.Instant;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShiftDto shiftDto = (ShiftDto) o;
        return Objects.equals(id, shiftDto.id) && Objects.equals(workerId, shiftDto.workerId) && Objects.equals(startTime, shiftDto.startTime) && Objects.equals(endTime, shiftDto.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workerId, startTime, endTime);
    }
}
