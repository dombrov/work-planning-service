package org.planning.server.api;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractIntegrationTest {

    @LocalServerPort
    protected int port;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected TestRestTemplate restTemplate;

    protected String baseUri;

    @BeforeEach
    void globalSetup() {
        baseUri = "http://localhost:" + port + "/api/v1";

        //DB cleanup
        jdbcTemplate.execute("delete from shifts");
        jdbcTemplate.execute("delete from workers");
    }

}
