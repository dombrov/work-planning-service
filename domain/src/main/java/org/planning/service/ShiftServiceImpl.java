package org.planning.service;

import org.planning.exceptions.EntityAlreadyExistException;
import org.planning.exceptions.ValidationException;
import org.planning.model.DayShift;
import org.planning.model.Shift;
import org.planning.repository.ShiftRepository;
import org.planning.repository.WorkerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Optional;

import static org.planning.Strings.isBlank;

public class ShiftServiceImpl implements ShiftService {

    private static final Logger LOG = LoggerFactory.getLogger(ShiftServiceImpl.class);

    private final ShiftRepository shiftRepository;
    private final WorkerRepository workerRepository;

    public ShiftServiceImpl(ShiftRepository shiftRepository, WorkerRepository workerRepository) {
        this.shiftRepository = shiftRepository;
        this.workerRepository = workerRepository;
    }

    @Override
    public Optional<Shift> get(String workerId, Long shiftId) {
        validateWorkerId(workerId);
        validateShiftId(shiftId);

        return shiftRepository.findBy(workerId, shiftId);
    }

    @Override
    public Collection<Shift> getShifts(String workerId, Instant from, Instant to) {
        validateWorkerId(workerId);
        if (from == null || to == null) {
            throw new IllegalArgumentException("Required from and to arguments");
        }
        return shiftRepository.findShifts(workerId, from, to);
    }

    @Override
    public Shift add(Instant date, DayShift dayShift, String workerId) {
        validateWorkerId(workerId);

        if (date == null) {
            throw new ValidationException("Date is required when add new shift");
        }

        if (dayShift == null) {
            throw new ValidationException("Day shift number is required");
        }

        if (workerRepository.findById(workerId).isEmpty()) {
            throw new ValidationException("Invalid worker with id = " + workerId);
        }

        if (existShiftOnSameDay(workerId, date)) {
            throw new EntityAlreadyExistException("Shift already exist for date " + date.atZone(ZoneId.of("GMT")).toLocalDate());
        }

        Shift shift = Shift.of(date, dayShift, workerId);

        if (shiftRepository.findBy(shift.getWorkerId(), shift.getId()).isPresent()) {
            throw new EntityAlreadyExistException("Shift already exist");
        }

        return shiftRepository.save(shift);
    }

    @Override
    public void delete(String workerId, Long shiftId) {
        validateWorkerId(workerId);
        validateShiftId(shiftId);

        shiftRepository.delete(workerId, shiftId);
    }

    private void validateShiftId(Long shiftId) {
        if (shiftId == null) {
            throw new ValidationException("Shift id required");
        }
    }

    private void validateWorkerId(String workerId) {
        if (isBlank(workerId)) {
            throw new ValidationException("Worker id required");
        }
    }

    private boolean existShiftOnSameDay(String workerId, Instant day) {
        Instant dayStart = day.truncatedTo(ChronoUnit.DAYS);
        Instant nextDayStart = dayStart.plus(1, ChronoUnit.DAYS);
        return shiftRepository.findShifts(workerId, dayStart, nextDayStart).isEmpty() == false;
    }

}
