package org.planning.repository.jdbc;

import org.planning.model.Worker;
import org.planning.repository.AbstractJdbcRepository;
import org.planning.repository.WorkerRepository;
import org.planning.repository.jdbc.mappers.WorkerRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

public class JdbcWorkerRepository extends AbstractJdbcRepository implements WorkerRepository {

    public JdbcWorkerRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Optional<Worker> findById(String id) {
        return jdbcTemplate.queryForStream("select * from workers where id=?", new WorkerRowMapper(), id).findFirst();
    }

    @Override
    public Worker save(Worker worker) {
        jdbcTemplate.update("insert into workers (id, first_name, last_name, active) values (?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        " first_name = ?, last_name = ?, active = ?",
                worker.getId(),
                worker.getFirstName(),
                worker.getLastName(),
                worker.isActive(),
                worker.getFirstName(),
                worker.getLastName(),
                worker.isActive()
        );
        return worker;
    }
}
