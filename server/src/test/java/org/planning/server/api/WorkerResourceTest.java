package org.planning.server.api;

import org.junit.jupiter.api.Test;
import org.planning.server.api.dto.WorkerDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class WorkerResourceTest extends AbstractIntegrationTest {

    @Test
    void testGet_whenWorkerDoesNotExist_thenNotFoundResponse() {
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

}