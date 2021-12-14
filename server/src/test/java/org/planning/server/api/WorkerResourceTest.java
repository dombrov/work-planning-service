package org.planning.server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.planning.model.Worker;
import org.planning.repository.WorkerRepository;
import org.planning.server.api.v1.dto.WorkerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

class WorkerResourceTest extends AbstractIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    protected WorkerRepository workerRepository;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("delete from workers");
    }

    @Test
    void testGet_whenWorkerDoesNotExist_thenExpectNotFoundResponse() {
        ResponseEntity<WorkerDto> response = restTemplate.getForEntity(baseUri + "/workers/404", WorkerDto.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testCreateWorkerEntity() {
        WorkerDto request = new WorkerDto();
        request.id = "john.doe";
        request.firstName = "John";
        request.lastName = "Doe";
        request.active = true;

        ResponseEntity<WorkerDto> response = restTemplate.postForEntity(baseUri + "/workers", request, WorkerDto.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        //check get method
        response = restTemplate.getForEntity(baseUri + "/workers/john.doe", WorkerDto.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testCreateWorker_givenWorkerExist_thenExpectConflictResponseCode() {
        Worker worker = givenPersistedWorker();

        WorkerDto request = new WorkerDto();
        request.id = worker.getId();
        request.firstName = worker.getFirstName();
        request.lastName = worker.getLastName();
        request.active = worker.isActive();

        ResponseEntity<WorkerDto> response = restTemplate.postForEntity(baseUri + "/workers", request, WorkerDto.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void testCreateWorker_givenMissingId_thenExpectBadRequestResponseCode() {
        WorkerDto request = new WorkerDto();

        ResponseEntity<WorkerDto> response = restTemplate.postForEntity(baseUri + "/workers", request, WorkerDto.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testUpdateWorker() {
        Worker worker = givenPersistedWorker();

        WorkerDto request = new WorkerDto();
        request.id = worker.getId();
        request.firstName = worker.getFirstName() + "_1";
        request.lastName = worker.getLastName() + "_1";
        request.active = !worker.isActive();

        restTemplate.put(baseUri + "/workers/{workerID}", request, worker.getId());

        ResponseEntity<WorkerDto> response = restTemplate.getForEntity(baseUri + "/workers/john.doe", WorkerDto.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(request);
    }

    private Worker givenPersistedWorker() {
        return workerRepository.save(new Worker("john.doe", "John", "Doe", true));
    }

}