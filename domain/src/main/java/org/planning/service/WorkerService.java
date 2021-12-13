package org.planning.service;

import org.planning.model.Worker;

import java.util.Optional;

public interface WorkerService {

    Optional<Worker> get(String id);

    Worker add(Worker worker);

    Worker update(Worker worker);

}
