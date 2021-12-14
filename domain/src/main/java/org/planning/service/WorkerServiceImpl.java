package org.planning.service;

import org.planning.exceptions.EntityAlreadyExistException;
import org.planning.exceptions.NotFoundException;
import org.planning.exceptions.ValidationException;
import org.planning.model.Worker;
import org.planning.repository.WorkerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.planning.Strings.isBlank;

public class WorkerServiceImpl implements WorkerService {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerService.class);


    private final WorkerRepository workerRepository;

    public WorkerServiceImpl(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

    @Override
    public Optional<Worker> get(String id) {
        return workerRepository.findById(id);
    }

    @Override
    public Worker add(Worker worker) {
        if (isBlank(worker.getId())) {
            throw new ValidationException("Worker id is required");
        }
        if (isBlank(worker.getFirstName())) {
            throw new ValidationException("First name required");
        }
        if (isBlank(worker.getLastName())) {
            throw new ValidationException("Last name required");
        }

        if (workerRepository.findById(worker.getId()).isPresent()) {
            throw new EntityAlreadyExistException("Worker already exist");
        }

        Worker persistedWorker = workerRepository.save(worker);
        LOG.trace("Created new worker: {}", persistedWorker);
        return persistedWorker;
    }

    @Override
    public Worker update(Worker worker) {
        if (workerRepository.findById(worker.getId()).isEmpty()) {
            throw new NotFoundException("Not found worker id=" + worker.getId());
        }
        worker = workerRepository.save(worker);
        LOG.trace("De-activated worker: {}", worker);
        return worker;
    }

}
