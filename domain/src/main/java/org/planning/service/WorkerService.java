package org.planning.service;

import org.planning.model.Worker;

public interface WorkerService {

    Worker get(String id);

    Worker add(Worker worker);

    Worker update(Worker worker);

}
