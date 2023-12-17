package com.connectcrew.teamone.projectservice;

import com.connectcrew.teamone.projectservice.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestConfig.class)
@SpringBootTest
class ProjectServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
