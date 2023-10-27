package com.connectcrew.teamone.projectservice;

import com.connectcrew.teamone.projectservice.config.TestR2dbcConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestR2dbcConfig.class)
class ProjectServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
