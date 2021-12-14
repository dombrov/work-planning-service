package org.planning.repository.memory;

import org.planning.model.Shift;
import org.planning.repository.ShiftRepository;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryShiftRepository implements ShiftRepository {

    private final Map<String, Map<Long, Shift>> repo = new ConcurrentHashMap<>();

    @Override
    public Optional<Shift> findBy(String workerId, Long id) {
        Map<Long, Shift> shifts = repo.get(workerId);
        if (shifts == null) {
            return Optional.empty();
        }
        return Optional.of(shifts.get(id));
    }

    @Override
    public Collection<Shift> findShifts(String workerId, Instant fromDate, Instant toDate) {
        Map<Long, Shift> shifts = repo.get(workerId);
        if (shifts == null) {
            return List.of();
        }
        return shifts.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .filter(entry -> entry.getStartTime().getEpochSecond() >= fromDate.getEpochSecond())
                .filter(entry -> entry.getStartTime().getEpochSecond() < toDate.getEpochSecond())
                .collect(Collectors.toList());
    }

    @Override
    public Shift save(Shift shift) {
        Map<Long, Shift> shifts = repo.computeIfAbsent(shift.getWorkerId(), k -> new ConcurrentHashMap<>());
        shifts.put(shift.getId(), shift);
        return shift;
    }

    @Override
    public void delete(String workerId, Long id) {
        Map<Long, Shift> shifts = repo.get(workerId);
        if (shifts != null) {
            shifts.remove(id);
        }
    }
}
