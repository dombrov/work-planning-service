package org.planning.repository.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.planning.model.Worker;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcWorkerEntityRepositoryTest extends JdbcTestContext {

    private JdbcWorkerEntityRepository repository;

    @BeforeEach
    void setup() {
        repository = new JdbcWorkerEntityRepository(jdbcTemplate);
    }

    @Test
    void testGetById_givenPersistedWorker() {
        Optional<Worker> worker = repository.getById("test.user");
        assertThat(worker).isNotEmpty();
        assertThat(worker.get().getFirstName()).isEqualTo("Test");
        assertThat(worker.get().getLastName()).isEqualTo("User");
        assertThat(worker.get().isActive()).isEqualTo(true);
    }

    @Test
    void testGetById_givenMissingWorker() {
        Optional<Worker> worker = repository.getById("test.user.missing");
        assertThat(worker).isEmpty();
    }

    @Test
    void testAdd() {
        //given
        assertThat(repository.getById("john.doe")).isEmpty();

        Worker expectedWorker = new Worker("john.doe", "John", "Doe", true);
        Worker persistedWorker = repository.save(expectedWorker);
        assertThat(persistedWorker).isEqualTo(expectedWorker);

        assertThat(repository.getById("john.doe")).isNotEmpty();
    }

}