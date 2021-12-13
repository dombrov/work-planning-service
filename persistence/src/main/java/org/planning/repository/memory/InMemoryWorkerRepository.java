package org.planning.repository.memory;

import org.planning.model.Worker;
import org.planning.repository.WorkerRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryWorkerRepository implements WorkerRepository {

    private final Map<String, Worker> repo = new ConcurrentHashMap<>();

    @Override
    public Optional<Worker> getById(String id) {
        return Optional.ofNullable(repo.get(id));
    }

    @Override
    public Worker save(Worker worker) {
        repo.put(worker.getId(), worker);
        return worker;
    }
}
