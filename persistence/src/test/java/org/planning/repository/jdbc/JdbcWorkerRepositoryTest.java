package org.planning.repository.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.planning.model.Worker;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcWorkerRepositoryTest extends JdbcTestContext {

    private JdbcWorkerRepository repository;

    @BeforeEach
    void setup() {
        repository = new JdbcWorkerRepository(jdbcTemplate);
    }

    @Test
    void testGetById_givenPersistedWorker() {
        Optional<Worker> worker = repository.findById("test.user");
        assertThat(worker).isNotEmpty();
        assertThat(worker.get().getFirstName()).isEqualTo("Test");
        assertThat(worker.get().getLastName()).isEqualTo("User");
        assertThat(worker.get().isActive()).isEqualTo(true);
    }

    @Test
    void testGetById_givenMissingWorker() {
        Optional<Worker> worker = repository.findById("test.user.missing");
        assertThat(worker).isEmpty();
    }

    @Test
    void testAdd() {
        //given
        assertThat(repository.findById("john.doe")).isEmpty();

        Worker expectedWorker = new Worker("john.doe", "John", "Doe", true);
        Worker persistedWorker = repository.save(expectedWorker);
        assertThat(persistedWorker).isEqualTo(expectedWorker);

        assertThat(repository.findById("john.doe")).isNotEmpty();
    }

    @Test
    void testUpdateWorker() {
        //given
        Worker worker = new Worker("john.doe.1", "John", "Doe", true);
        repository.save(worker);

        Worker workerToUpdate = new Worker(worker.getId(), "Johnny", "Doe", false);
        Worker updated = repository.save(workerToUpdate);
        assertThat(updated).isNotNull();
        assertThat(updated).isEqualTo(workerToUpdate);

        Optional<Worker> workerOp = repository.findById(worker.getId());
        assertThat(workerOp).isNotEmpty();
        assertThat(workerOp.get()).isEqualTo(workerToUpdate);
    }

}