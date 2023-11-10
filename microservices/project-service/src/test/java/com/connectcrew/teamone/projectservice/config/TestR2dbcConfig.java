package com.connectcrew.teamone.projectservice.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.autoconfigure.data.r2dbc.R2dbcDataAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;

@TestConfiguration
public class TestR2dbcConfig {

    @MockBean
    private ConnectionFactory connectionFactory;

    @MockBean
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @MockBean
    private DatabaseClient r2dbcDatabaseClient;

    @MockBean
    private R2dbcDataAutoConfiguration r2dbcDataAutoConfiguration;

    @MockBean
    private R2dbcCustomConversions r2dbcCustomConversions;
}
