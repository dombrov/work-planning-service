package org.planning.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Shift {

    public static Shift of(Instant date, DayShift dayShift, String workerId) {
        Instant startTime = dayShift.startTimeOf(date);
        Long id = startTime.getEpochSecond();
        return new Shift(id, workerId, startTime);
    }

    private final Long id;
    private final String workerId;
    private final Instant startTime;

    public Shift(Long id, String workerId, Instant startTime) {
        this.id = id;
        this.workerId = workerId;
        this.startTime = startTime;
    }

    public Long getId() {
        return id;
    }

    public String getWorkerId() {
        return workerId;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return startTime.plus(DayShift.SHIFT_DURATION, ChronoUnit.HOURS);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shift shift = (Shift) o;
        return Objects.equals(id, shift.id) && Objects.equals(startTime, shift.startTime) && Objects.equals(workerId, shift.workerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTime, workerId);
    }

    @Override
    public String toString() {
        return "Shift{" +
                "id='" + id + '\'' +
                ", workerId='" + workerId + '\'' +
                ", startTime=" + startTime +
                '}';
    }
}
