package org.planning.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.planning.exceptions.EntityAlreadyExistException;
import org.planning.exceptions.ValidationException;
import org.planning.model.DayShift;
import org.planning.model.Shift;
import org.planning.model.Worker;
import org.planning.repository.ShiftRepository;
import org.planning.repository.WorkerRepository;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ShiftServiceImplTest {

    @Mock
    private ShiftRepository shiftRepository;
    @Mock
    private WorkerRepository workerRepository;

    private ShiftService shiftService;

    private DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE
            .withZone(ZoneId.systemDefault());


    @BeforeEach
    void setUp() {
        shiftService = new ShiftServiceImpl(shiftRepository, workerRepository);
        Mockito.reset(shiftRepository, workerRepository);
    }

    @Test
    void get_givenEmptyRepository_thenReturnNull() {
        assertThat(shiftService.get("workerId", 1L)).isEmpty();
    }

    @Test
    void get_givenMissingWorkerId() {
        //workerId = null
        assertThrows(ValidationException.class,
                () -> shiftService.get(null, 1L),
                "ValidationException is expected");

        //workerId = empty
        assertThrows(ValidationException.class,
                () -> shiftService.get("", 1L),
                "ValidationException is expected");
    }

    @Test
    void get_givenMissingShiftId() {
        assertThrows(ValidationException.class,
                () -> shiftService.get("workerId", null),
                "ValidationException is expected");

    }

    @Test
    void get_givenPersistedShift_thenShiftById() {
        Shift expectedShift = new Shift(1L, "john.doe", Instant.now());
        stubPersistedShift(expectedShift);

        Optional<Shift> shift = shiftService.get(expectedShift.getWorkerId(), expectedShift.getId());
        assertThat(shift).isNotEmpty();
        assertThat(shift.get()).isEqualTo(expectedShift);
    }

    @Test
    void add_givenUndefinedDate_thenThrowValidationException() {
        assertThrows(ValidationException.class,
                () -> shiftService.add(null, DayShift.FIRST, "john.doe"),
                "ValidationException is expected");
    }

    @Test
    void add_givenUndefinedDateShift_thenThrowValidationException() {
        assertThrows(ValidationException.class,
                () -> shiftService.add(Instant.now(), null, "john.doe"),
                "ValidationException is expected");
    }

    @Test
    void add_givenMissingWorker_thenThrowValidationException() {
        Mockito.when(workerRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

        assertThrows(ValidationException.class,
                () -> shiftService.add(Instant.now(), DayShift.FIRST, "john.doe"),
                "ValidationException is expected");
    }

    @Test
    void add_givenValidParameters_thenCreateShiftSuccessfully() {
        String workerId = "john.doe";

        //given mocks
        Mockito.when(workerRepository.findById(Mockito.anyString())).thenReturn(Optional.of(new Worker(workerId, null, null, false)));
        stubOnAddShiftToReturnIdentity();

        Instant date = Instant.now();
        DayShift dayShift = DayShift.FIRST;

        Shift shift = shiftService.add(date, dayShift, workerId);
        assertThat(shift).isNotNull();
        assertThat(shift).isEqualTo(Shift.of(date, dayShift, workerId));
    }

    @Test
    void add_givenShiftAlreadyExistInRepository_thenThrowException() {
        Instant date = Instant.now();
        DayShift dayShift = DayShift.FIRST;
        String workerId = "john.doe";

        //given mocks
        Mockito.when(workerRepository.findById(Mockito.anyString())).thenReturn(Optional.of(new Worker(workerId, null, null, false)));
        stubPersistedShift(Shift.of(date, dayShift, workerId));

        assertThrows(EntityAlreadyExistException.class,
                () -> shiftService.add(date, dayShift, workerId),
                "ValidationException is expected");
    }

    @Test
    void add_givenAShiftForTheSameDay_thenThrowException() {
        Instant date = Instant.now();

        //given mocks
        Shift firstShift = Shift.of(date, DayShift.FIRST, "john.doe");
        Mockito.when(workerRepository.findById(Mockito.anyString())).thenReturn(Optional.of(new Worker(firstShift.getWorkerId(), null, null, false)));
        Mockito.when(shiftRepository.findShifts(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(List.of(firstShift));

        assertThrows(EntityAlreadyExistException.class,
                () -> shiftService.add(date, DayShift.SECOND, "john.doe"),
                "ValidationException is expected");
    }

    @Test
    void add_givenMissingWorkerId() {
        //shiftId = null
        assertThrows(ValidationException.class,
                () -> shiftService.add(Instant.now(), DayShift.FIRST, null),
                "ValidationException is expected");

        //shiftId = empty
        assertThrows(ValidationException.class,
                () -> shiftService.add(Instant.now(), DayShift.FIRST, ""),
                "ValidationException is expected");
    }

    @Test
    void delete_givenPersistedShift_thenDeleteSuccessfully() {
        Shift shift = Shift.of(Instant.now(), DayShift.FIRST, "john.doe");

        //given mocks
        stubPersistedShift(shift);
        Mockito.doAnswer(i -> {
            Mockito.when(shiftRepository.findBy(Mockito.eq(shift.getWorkerId()), Mockito.eq(shift.getId()))).thenReturn(Optional.empty());
            return null;
        }).when(shiftRepository).delete(Mockito.eq(shift.getWorkerId()), Mockito.eq(shift.getId()));

        //validate shift exist first
        assertThat(shiftService.get(shift.getWorkerId(), shift.getId())).isNotNull();

        shiftService.delete(shift.getWorkerId(), shift.getId());

        //validate get shift return null
        assertThat(shiftService.get(shift.getWorkerId(), shift.getId())).isEmpty();

    }

    @Test
    void delete_givenMissingWorkerId() {
        //workerId = null
        assertThrows(ValidationException.class,
                () -> shiftService.delete(null, 1L),
                "ValidationException is expected");

        //workerId = empty
        assertThrows(ValidationException.class,
                () -> shiftService.delete("", 1L),
                "ValidationException is expected");
    }

    @Test
    void delete_givenMissingShiftId() {
        assertThrows(ValidationException.class,
                () -> shiftService.delete("workerId", null),
                "ValidationException is expected");
    }

    private void stubPersistedShift(Shift shift) {
        Mockito.when(shiftRepository.findBy(Mockito.eq(shift.getWorkerId()), Mockito.eq(shift.getId())))
                .thenReturn(Optional.of(shift));
    }

    private void stubOnAddShiftToReturnIdentity() {
        //given creating new worker mock
        Mockito.when(shiftRepository.save(Mockito.any(Shift.class))).thenAnswer(i -> {
            Shift shift = i.getArgument(0);
            return shift;
        });
    }

}