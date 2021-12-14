package org.planning.service;

import org.planning.model.DayShift;
import org.planning.model.Shift;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

public interface ShiftService {

    Optional<Shift> get(String workerId, Long shiftId);

    Collection<Shift> getShifts(String workerId, Instant from, Instant to);

    Shift add(Instant date, DayShift dayShift, String workerId);

    void delete(String workerId, Long shiftId);

}
