package org.planning.repository.jdbc;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@AutoConfigureJdbc
@AutoConfigureTestDatabase
@Sql({"classpath:schema.sql", "classpath:test-data.sql"})
public class JdbcTestContext {

    @Autowired
    protected JdbcTemplate jdbcTemplate;


}
