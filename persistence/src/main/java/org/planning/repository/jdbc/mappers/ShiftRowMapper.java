package org.planning.repository.jdbc.mappers;

import org.planning.model.Shift;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public class ShiftRowMapper implements RowMapper<Shift> {

    @Override
    public Shift mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long id = rs.getLong("id");
        String workerId = rs.getString("worker_id");
        Instant startTime = rs.getTimestamp("start_time").toInstant();
        return new Shift(id, workerId, startTime);
    }
}
