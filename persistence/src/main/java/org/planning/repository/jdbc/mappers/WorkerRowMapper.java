package org.planning.repository.jdbc.mappers;

import org.planning.model.Worker;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkerRowMapper implements RowMapper<Worker> {

    @Override
    public Worker mapRow(ResultSet rs, int rowNum) throws SQLException {
        String id = rs.getString("id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        boolean active = rs.getBoolean("active");
        return new Worker(id, firstName, lastName, active);
    }
}
