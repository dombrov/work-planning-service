package org.planning.repository;

import org.springframework.jdbc.core.JdbcTemplate;

public abstract class AbstractJdbcRepository {

    protected JdbcTemplate jdbcTemplate;

    public AbstractJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
