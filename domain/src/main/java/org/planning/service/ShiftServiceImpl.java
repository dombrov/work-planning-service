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

        Optional<Shift> shift = shiftRepository.findBy(workerId, shiftId);
        LOG.trace("Find shift by workerId={}, shiftId={}, result = {}", workerId, shift, shift);
        return shift;
    }

    @Override
    public Collection<Shift> getShifts(Instant from, Instant to) {
        if (from == null || to == null) {
            LOG.debug("Find shifts: Required from and to parameters. From={}, To={}", from, to);
            throw new IllegalArgumentException("Required from and to arguments");
        }
        Collection<Shift> shifts = shiftRepository.findShifts(from, to);
        LOG.trace("Find shifts from={}, to={}, result = {}", from, to, shifts);
        return shifts;
    }

    @Override
    public Collection<Shift> getShifts(String workerId, Instant from, Instant to) {
        validateWorkerId(workerId);
        if (from == null || to == null) {
            LOG.debug("Find shifts: Required from and to parameters. From={}, To={}", from, to);
            throw new IllegalArgumentException("Required from and to arguments");
        }
        Collection<Shift> shifts = shiftRepository.findShifts(workerId, from, to);
        LOG.trace("Find shifts workerId={}, from={}, to={}, result = {}", workerId, from, to, shifts);
        return shifts;
    }

    @Override
    public Shift add(Instant date, DayShift dayShift, String workerId) {
        validateWorkerId(workerId);
        if (date == null) {
            LOG.debug("Add new shift: Date is required when add new shift");
            throw new ValidationException("Date is required when add new shift");
        }

        if (dayShift == null) {
            LOG.debug("Add new shift: Day shift number is required");
            throw new ValidationException("Day shift number is required");
        }

        if (workerRepository.findById(workerId).isEmpty()) {
            LOG.debug("Add new shift: Invalid worker id = {}", workerId);
            throw new ValidationException("Invalid worker with id = " + workerId);
        }

        if (existShiftOnSameDay(workerId, date)) {
            LOG.debug("Add new shift: found a persisted shift for date {}", date.truncatedTo(ChronoUnit.DAYS));
            throw new EntityAlreadyExistException("Shift already exist for date " + date.atZone(ZoneId.of("GMT")).toLocalDate());
        }

        Shift shift = Shift.of(date, dayShift, workerId);

        Optional<Shift> persistedShift = shiftRepository.findBy(shift.getWorkerId(), shift.getId());
        if (persistedShift.isPresent()) {
            LOG.debug("Shift already exist: {}", persistedShift.get());
            throw new EntityAlreadyExistException("Shift already exist");
        }

        shiftRepository.save(shift);
        LOG.trace("Added new shift: {}", shift);
        return shift;
    }

    @Override
    public void delete(String workerId, Long shiftId) {
        validateWorkerId(workerId);
        validateShiftId(shiftId);

        shiftRepository.delete(workerId, shiftId);
        LOG.trace("Deleted workerId={}, shiftId={}", workerId, shiftId);
    }

    private void validateShiftId(Long shiftId) {
        if (shiftId == null) {
            LOG.debug("Shift id required");
            throw new ValidationException("Shift id required");
        }
    }

    private void validateWorkerId(String workerId) {
        if (isBlank(workerId)) {
            LOG.debug("Worker id required");
            throw new ValidationException("Worker id required");
        }
    }

    private boolean existShiftOnSameDay(String workerId, Instant day) {
        Instant dayStart = day.truncatedTo(ChronoUnit.DAYS);
        Instant nextDayStart = dayStart.plus(1, ChronoUnit.DAYS);
        return getShifts(workerId, dayStart, nextDayStart).isEmpty() == false;
    }

}
