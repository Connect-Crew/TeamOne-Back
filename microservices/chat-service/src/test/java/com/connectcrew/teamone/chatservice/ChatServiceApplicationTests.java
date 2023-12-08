package com.connectcrew.teamone.chatservice;

import com.connectcrew.teamone.chatservice.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestConfig.class)
class ChatServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
