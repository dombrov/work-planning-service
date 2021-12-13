package org.planning.repository;

import org.planning.model.Worker;

import java.util.Optional;

public interface WorkerRepository {

    Optional<Worker> getById(String id);

    Worker save(Worker worker);

}
