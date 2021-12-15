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
    public Optional<Worker> findBy(String id) {
        Optional<Worker> worker = workerRepository.findById(id);
        LOG.trace("Find worker by id={}, result = {}", id, worker);
        return worker;
    }

    @Override
    public Worker add(Worker worker) {

        if (isBlank(worker.getId())) {
            LOG.debug("Worker id is required. Add request: {}", worker);
            throw new ValidationException("Worker id is required");
        }
        if (isBlank(worker.getFirstName())) {
            LOG.debug("First name required. Add request: {}", worker);
            throw new ValidationException("First name required");
        }
        if (isBlank(worker.getLastName())) {
            LOG.debug("Last name required. Add request: {}", worker);
            throw new ValidationException("Last name required");
        }

        if (workerRepository.findById(worker.getId()).isPresent()) {
            String message = String.format("Worker already exist: %s", worker.getId());
            LOG.debug(message);
            throw new EntityAlreadyExistException(message);
        }

        Worker persistedWorker = workerRepository.save(worker);
        LOG.trace("Created new worker: {}", persistedWorker);
        return persistedWorker;
    }

    @Override
    public Worker update(Worker worker) {
        if (workerRepository.findById(worker.getId()).isEmpty()) {
            String message = String.format("Not found worker to update: %s", worker);
            LOG.debug(message);
            throw new NotFoundException(message);
        }
        worker = workerRepository.save(worker);
        LOG.trace("Updated worker: {}", worker);
        return worker;
    }

}
