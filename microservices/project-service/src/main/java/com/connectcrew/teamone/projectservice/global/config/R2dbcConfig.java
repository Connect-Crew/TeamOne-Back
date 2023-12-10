package com.connectcrew.teamone.projectservice.global.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
public class R2dbcConfig {
    @Bean
    public ConnectionFactory connectionFactory(
            @Value("${db.host}") String host,
            @Value("${db.port}") int port,
            @Value("${db.id}") String id,
            @Value("${db.pw}") String pw,
            @Value("${db.database}") String database
    ) {
        ConnectionFactoryOptions options = ConnectionFactoryOptions.builder()
                .option(DRIVER, "mysql") // 데이터베이스 드라이버
                .option(HOST, host) // 데이터베이스 호스트명
                .option(PORT, port) // 데이터베이스 포트 번호
                .option(DATABASE, database) // 데이터베이스 이름
                .option(USER, id) // 데이터베이스 사용자명
                .option(PASSWORD, pw) // 데이터베이스 비밀번호
                .option(SSL, false)
                .build();

        return ConnectionFactories.get(options);
    }

    @Bean
    public R2dbcEntityTemplate r2dbcEntityTemplate(ConnectionFactory connectionFactory) {
        return new R2dbcEntityTemplate(connectionFactory);
    }

    @Bean
    public DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
        return DatabaseClient.create(connectionFactory);
    }
}
