package com.connectcrew.teamone.compositeservice;

import com.connectcrew.teamone.compositeservice.config.TestBeanConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestBeanConfig.class)
class CompositeServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
