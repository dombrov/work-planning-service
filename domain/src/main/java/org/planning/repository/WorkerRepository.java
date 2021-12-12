package org.planning.repository;

import org.planning.model.Worker;

public interface WorkerRepository {

    Worker get(String id);

    boolean exist(String id);

    Worker save(Worker worker);

}
