package org.planning.server;

import org.planning.repository.ShiftRepository;
import org.planning.repository.WorkerRepository;
import org.planning.repository.jdbc.JdbcShiftRepository;
import org.planning.repository.jdbc.JdbcWorkerRepository;
import org.planning.repository.memory.InMemoryShiftRepository;
import org.planning.repository.memory.InMemoryWorkerRepository;
import org.planning.service.ShiftService;
import org.planning.service.ShiftServiceImpl;
import org.planning.service.WorkerService;
import org.planning.service.WorkerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
public class ApplicationContext {

/*
    @Bean
    public WorkerRepository getWorkerRepository() {
        return new InMemoryWorkerRepository();
    }

    @Bean
    public ShiftRepository getShiftRepository() {
        return new InMemoryShiftRepository();
    }
*/

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("worker_shifts;DATABASE_TO_UPPER=false;MODE=MySQL")
                .addScript("classpath:schema.sql")
                .build();
    }

    @Bean
    public JdbcTemplate getJdbcTemplate(@Autowired DataSource dataSource) {
        return new JdbcTemplate(dataSource);

    }

    @Bean
    public WorkerRepository getWorkerRepository(@Autowired JdbcTemplate jdbcTemplate) {
        return new JdbcWorkerRepository(jdbcTemplate);
    }

    @Bean
    public ShiftRepository getShiftRepository(@Autowired JdbcTemplate jdbcTemplate) {
        return new JdbcShiftRepository(jdbcTemplate);
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
