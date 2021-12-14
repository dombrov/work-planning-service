package org.planning.repository;

import org.planning.model.Shift;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

public interface ShiftRepository {

    Optional<Shift> findBy(String workerId, Long shiftId);

    Collection<Shift> findShifts(String workerId, Instant fromDate, Instant toDate);

    Shift save(Shift shift);

    void delete(String workerId, Long shiftId);

}
