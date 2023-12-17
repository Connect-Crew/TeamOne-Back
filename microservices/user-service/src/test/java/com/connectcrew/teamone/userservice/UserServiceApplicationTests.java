package com.connectcrew.teamone.userservice;

import com.connectcrew.teamone.userservice.config.TestBeanConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestBeanConfig.class)
class UserServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
