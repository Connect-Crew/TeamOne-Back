package com.connectcrew.teamone.chatservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@SpringBootTest
class ChatServiceApplicationTests {

	@MockBean
	RedisConnectionFactory redisConnectionFactory;

	@MockBean
	RedisTemplate<String, Object> redisTemplate;

	@MockBean
	RedisMessageListenerContainer redisMessageListenerContainer;

	@MockBean
	RedisKeyValueAdapter redisKeyValueAdapter;
	@Test
	void contextLoads() {
	}

}
