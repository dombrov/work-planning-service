package org.planning.repository.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

public class JdbcTestContext {

    protected JdbcTemplate jdbcTemplate;

    public JdbcTestContext() {
        this.jdbcTemplate = new JdbcTemplate(dataSource());
    }

    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("testdb;DATABASE_TO_UPPER=false;MODE=MySQL")
                .addScript("classpath:schema.sql")
                .addScript("classpath:test-data.sql").build();
    }

}
