package org.planning.server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.planning.model.DayShift;
import org.planning.model.Shift;
import org.planning.model.Worker;
import org.planning.repository.ShiftRepository;
import org.planning.repository.WorkerRepository;
import org.planning.server.api.v1.dto.ShiftCreateRequestDto;
import org.planning.server.api.v1.dto.ShiftDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

class WorkerShiftsResourceTest extends AbstractIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    protected WorkerRepository workerRepository;

    @Autowired
    protected ShiftRepository shiftRepository;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("delete from shifts");
        jdbcTemplate.execute("delete from workers");
    }

    @Test
    void getWorkerShift_givenMissingWorker_thenExpectNotFoundResponse() {
        ResponseEntity<ShiftDto> response = restTemplate.getForEntity(baseUri + "/workers/404/shifts/404", ShiftDto.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getWorkerShift_givenValidWorkerAndMissingShift_thenExpectNotFoundResponse() {
        //given
        Worker worker = givenPersistedWorker();

        ResponseEntity<ShiftDto> response = restTemplate.getForEntity(baseUri + "/workers/{worker}/shifts/404", ShiftDto.class, worker.getId());
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getWorkerShift_givenPersistedShift_thenExpectOkResponse() {
        //given
        Worker worker = givenPersistedWorker();
        Shift shift = Shift.of(Instant.now(), DayShift.SECOND, worker.getId());
        givenPersistedShift(shift);

        ResponseEntity<ShiftDto> response = restTemplate.getForEntity(baseUri + "/workers/{worker}/shifts/{shiftId}", ShiftDto.class, shift.getWorkerId(), shift.getId());
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(ShiftDto.of(shift));
    }

    @Test
    void getWorkerShifts_givenNoShifts_thenExpectEmptyResponse() {
        Instant from = Instant.now().truncatedTo(ChronoUnit.DAYS);
        Instant to = from.plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);

        ResponseEntity<ShiftDto[]> response = restTemplate.getForEntity(baseUri + "/workers/{worker}/shifts?from={from}&to={to}", ShiftDto[].class,
                "john.doe", from, to);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getWorkerShifts_givenPersisted_thenExpectGettingShiftsForRequestedInterval() {
        //given
        Worker worker = givenPersistedWorker();
        Shift shift1 = Shift.of(Instant.now(), DayShift.SECOND, worker.getId());
        Shift shift2 = Shift.of(Instant.now().plus(1, ChronoUnit.DAYS), DayShift.FIRST, worker.getId());
        givenPersistedShift(shift1);
        givenPersistedShift(shift2);

        Instant from = shift1.getStartTime()
                .truncatedTo(ChronoUnit.DAYS);
        Instant to = from.plus(1, ChronoUnit.DAYS)
                .truncatedTo(ChronoUnit.DAYS);

        ResponseEntity<ShiftDto[]> response = restTemplate.getForEntity(baseUri + "/workers/{worker}/shifts?from={from}&to={to}", ShiftDto[].class,
                worker.getId(), from, to);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0]).isEqualTo(ShiftDto.of(shift1));
    }

    @Test
    void addWorkerShift() {
        //given
        Worker worker = givenPersistedWorker();

        ShiftCreateRequestDto requestDto = new ShiftCreateRequestDto();
        requestDto.date = Instant.now();
        requestDto.dayShift = DayShift.THIRD;
        ResponseEntity<ShiftDto> response = restTemplate.postForEntity(baseUri + "/workers/{workerId}/shifts", requestDto, ShiftDto.class, worker.getId());
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Shift expectedShift = Shift.of(requestDto.date, requestDto.dayShift, worker.getId());
        assertThat(response.getBody()).isEqualTo(ShiftDto.of(expectedShift));

        //validate with get request
        response = restTemplate.getForEntity(baseUri + "/workers/{workerId}/shifts/{shiftId}", ShiftDto.class, expectedShift.getWorkerId(), expectedShift.getId());
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(ShiftDto.of(expectedShift));
    }

    @Test
    void addWorkerShift_givenPersistedShiftForTheSameDay_thenExpectConflictResponse() {
        //given
        Worker worker = givenPersistedWorker();
        Shift shift = Shift.of(Instant.now(), DayShift.SECOND, worker.getId());
        givenPersistedShift(shift);

        ShiftCreateRequestDto requestDto = new ShiftCreateRequestDto();
        requestDto.date = shift.getStartTime();
        requestDto.dayShift = DayShift.THIRD;
        ResponseEntity<ShiftDto> response = restTemplate.postForEntity(baseUri + "/workers/{workerId}/shifts", requestDto, ShiftDto.class, worker.getId());
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void deleteWorkerShift_givenPersistedShift() {
        //given
        Worker worker = givenPersistedWorker();
        Shift shift = Shift.of(Instant.now(), DayShift.SECOND, worker.getId());
        givenPersistedShift(shift);

        restTemplate.delete(baseUri + "/workers/{workerId}/shifts/{shiftId}", shift.getWorkerId(), shift.getId());

        //check get not found
        ResponseEntity<ShiftDto> response = restTemplate.getForEntity(baseUri + "/workers/{worker}/shifts/{shiftId}", ShiftDto.class, shift.getWorkerId(), shift.getId());
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private Worker givenPersistedWorker() {
        return workerRepository.save(new Worker("john.doe", "John", "Doe", true));
    }

    private Shift givenPersistedShift(Shift shift) {
        return shiftRepository.save(shift);
    }

}