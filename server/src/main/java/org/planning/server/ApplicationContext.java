package org.planning.server;

import org.planning.repository.ShiftRepository;
import org.planning.repository.WorkerRepository;
import org.planning.repository.memory.InMemoryShiftRepository;
import org.planning.repository.memory.InMemoryWorkerRepository;
import org.planning.service.ShiftService;
import org.planning.service.ShiftServiceImpl;
import org.planning.service.WorkerService;
import org.planning.service.WorkerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationContext {

    @Bean
    public WorkerRepository getWorkerRepository() {
        return new InMemoryWorkerRepository();
    }

    @Bean
    public ShiftRepository getShiftRepository() {
        return new InMemoryShiftRepository();
    }

    @Bean
    public WorkerService getWorkerService(@Autowired WorkerRepository workerRepository) {
        return new WorkerServiceImpl(workerRepository);
    }

    @Bean
    public ShiftService getShiftService(@Autowired ShiftRepository shiftRepository,
                                        @Autowired WorkerRepository workerRepository) {
        return new ShiftServiceImpl(shiftRepository, workerRepository);
    }

}
