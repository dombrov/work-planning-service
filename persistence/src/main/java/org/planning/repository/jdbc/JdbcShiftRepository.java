package org.planning.repository.jdbc;

import org.planning.model.Shift;
import org.planning.repository.AbstractJdbcRepository;
import org.planning.repository.ShiftRepository;
import org.planning.repository.jdbc.mappers.ShiftRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class JdbcShiftRepository extends AbstractJdbcRepository implements ShiftRepository {

    public JdbcShiftRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Optional<Shift> findBy(String workerId, Long shiftId) {
        return jdbcTemplate.queryForStream("select * from shifts where id=? and worker_id=?",
                new ShiftRowMapper(), shiftId, workerId)
                .findFirst();
    }

    @Override
    public Collection<Shift> findShifts(String workerId, Instant fromDate, Instant toDate) {
        return jdbcTemplate.queryForStream("select * from shifts where worker_id=? and start_time >= ? and start_time < ?",
                        new ShiftRowMapper(), workerId, fromDate, toDate)
                .collect(Collectors.toList());
    }

    @Override
    public Shift save(Shift shift) {
        jdbcTemplate.update("insert into shifts (id, worker_id, start_time) values (?, ?, ?) ",
                shift.getId(),
                shift.getWorkerId(),
                shift.getStartTime()
        );
        return shift;
    }

    @Override
    public void delete(String workerId, Long shiftId) {
        jdbcTemplate.update("delete from shifts where id=? and worker_id=?",
                shiftId,
                workerId
        );
    }
}
