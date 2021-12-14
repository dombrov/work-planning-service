package org.planning.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.planning.exceptions.EntityAlreadyExistException;
import org.planning.exceptions.NotFoundException;
import org.planning.exceptions.ValidationException;
import org.planning.model.Worker;
import org.planning.repository.WorkerRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class WorkerServiceImplTest {

    @Mock
    private WorkerRepository workerRepository;

    private WorkerService workerService;

    @BeforeEach
    void setUp() {
        workerService = new WorkerServiceImpl(workerRepository);
        Mockito.reset(workerRepository);
    }

    @Test
    void get_givenMissingWorker_thenGetEmpty() {
        assertThat(workerService.get("john.doe")).isEmpty();
    }

    @Test
    void get_givenAWorker_thenGetWorkerProfileById() {
        String workerId = "john.doe";
        Worker expectedWorker = new Worker(workerId, "John", "Doe", true);
        stubPersistedWorker(expectedWorker);

        Optional<Worker> worker = workerService.get(workerId);
        assertThat(worker).isNotEmpty();
        assertThat(worker.get()).isEqualTo(expectedWorker);
    }

    @Test
    void get_givenAnExceptionThrownByRepository_thenPropagateFurther() {
        Mockito.when(workerRepository.findById(Mockito.anyString())).thenThrow(new RuntimeException("Test Error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> workerService.get("john.doe"), "RuntimeException is expected");
        assertThat(exception.getMessage()).isEqualTo("Test Error");
    }

    @Test
    void add_givenAllRequiredFields_thenCreateNewWorkerProfile() {
        //given creating new worker mock
        stubSaveWorkerToReturnIdentity();

        Worker workerToAdd = new Worker("john.doe", "John", "Doe", true);
        Worker worker = workerService.add(workerToAdd);
        assertThat(worker).isNotNull();
        assertThat(worker).isEqualTo(workerToAdd);
    }

    @Test
    void add_givenWorkerAlreadyExist_thenThrowException() {
        Worker worker = new Worker("john.doe", "John", "Doe", true);
        stubPersistedWorker(worker);
        assertThrows(EntityAlreadyExistException.class,
                () -> workerService.add(new Worker("john.doe", "John", "Doe", true)),
                "ValidationException is expected");
    }

    @Test
    void add_givenMissingId_thenThrowValidationException() {
        //first name = null
        assertThrows(ValidationException.class,
                () -> workerService.add(new Worker(null, "John", "Doe", true)),
                "ValidationException is expected");

        //first name is empty
        assertThrows(ValidationException.class,
                () -> workerService.add(new Worker("", "John", "Doe", true)),
                "ValidationException is expected");
    }

    @Test
    void add_givenMissingFirstName_thenThrowValidationException() {
        //first name = null
        assertThrows(ValidationException.class,
                () -> workerService.add(new Worker("john.doe", null, "Doe", true)),
                "ValidationException is expected");

        //first name is empty
        assertThrows(ValidationException.class,
                () -> workerService.add(new Worker("john.doe", "", "Doe", true)),
                "ValidationException is expected");
    }

    @Test
    void add_givenMissingLastName_thenThrowValidationException() {
        //last name = null
        assertThrows(ValidationException.class,
                () -> workerService.add(new Worker("john.doe", "John", null, true)),
                "ValidationException is expected");

        //last name is empty
        assertThrows(ValidationException.class,
                () -> workerService.add(new Worker("john.doe", "John", "", true)),
                "ValidationException is expected");
    }

    @Test
    void add_givenAnExceptionThrownByRepository_thenPropagateFurther() {
        Mockito.when(workerRepository.save(Mockito.any())).thenThrow(new RuntimeException("Test Error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> workerService.add(new Worker("john.doe", "John", "Doe", true)), "RuntimeException is expected");
        assertThat(exception.getMessage()).isEqualTo("Test Error");
    }

    @Test
    void update_givenValidWorkerProfile() {
        //given get worker mock
        Worker newWorker = new Worker("john.doe", "John", "Doe", true);
        stubPersistedWorker(newWorker);
        stubSaveWorkerToReturnIdentity();

        Worker updateRequest = new Worker(newWorker.getId(), newWorker.getFirstName(), newWorker.getLastName(), newWorker.isActive());

        Worker updatedWorker = workerService.update(updateRequest);
        assertThat(updatedWorker).isEqualTo(updateRequest);
    }

    @Test
    void update_givenMissingWorker_thenThrowNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> workerService.update(new Worker("john.doe", "John", "Doe", true)),
                "NotFoundException is expected");
    }



    private void stubPersistedWorker(Worker worker) {
        Mockito.when(workerRepository.findById(Mockito.eq(worker.getId()))).thenReturn(Optional.of(worker));
    }

    private void stubSaveWorkerToReturnIdentity() {
        Mockito.when(workerRepository.save(Mockito.any(Worker.class))).thenAnswer(i -> {
            Worker worker = i.getArgument(0);
            return worker;
        });
    }
}