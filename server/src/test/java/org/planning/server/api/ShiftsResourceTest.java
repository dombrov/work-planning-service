package org.planning.server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.planning.model.DayShift;
import org.planning.model.Shift;
import org.planning.model.Worker;
import org.planning.repository.ShiftRepository;
import org.planning.repository.WorkerRepository;
import org.planning.server.api.v1.dto.ShiftDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

class ShiftsResourceTest extends AbstractIntegrationTest {

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
    void getShifts_givenNoShifts_thenExpectEmptyResponse() {
        Instant from = Instant.now().truncatedTo(ChronoUnit.DAYS);
        Instant to = from.plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);

        ResponseEntity<ShiftDto[]> response = restTemplate.getForEntity(baseUri + "/shifts?from={from}&to={to}", ShiftDto[].class, from, to);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getShifts_givenPersisted_thenExpectGettingShiftsForRequestedInterval() {
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

        ResponseEntity<ShiftDto[]> response = restTemplate.getForEntity(baseUri + "/shifts?from={from}&to={to}", ShiftDto[].class, from, to);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0]).isEqualTo(ShiftDto.of(shift1));
    }

    private Worker givenPersistedWorker() {
        return workerRepository.save(new Worker("john.doe", "John", "Doe", true));
    }

    private Shift givenPersistedShift(Shift shift) {
        return shiftRepository.save(shift);
    }


}