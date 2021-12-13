package org.planning.repository.jdbc;

import org.planning.model.Worker;
import org.planning.repository.WorkerRepository;
import org.planning.repository.jdbc.mappers.WorkerRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

public class JdbcWorkerEntityRepository implements WorkerRepository {

    private JdbcTemplate jdbcTemplate;

    public JdbcWorkerEntityRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Worker> getById(String id) {
        return jdbcTemplate.queryForStream("select * from workers where id=?", new WorkerRowMapper(), id).findFirst();
    }

    @Override
    public Worker save(Worker worker) {
        jdbcTemplate.update("insert into workers (id, first_name, last_name, active) values (?, ?, ?, ?)", worker.getId(), worker.getLastName(), worker.getLastName(), worker.isActive());
        return worker;
    }
}
