package org.planning.repository;

import org.planning.model.Shift;

import java.time.Instant;
import java.util.Collection;

public interface ShiftRepository {

    Shift get(String workerId, Long shiftId);

    Collection<Shift> getShifts(String workerId, Instant fromDate, Instant toDate);

    Shift save(Shift shift);

    Shift delete(String workerId, Long shiftId);

}
