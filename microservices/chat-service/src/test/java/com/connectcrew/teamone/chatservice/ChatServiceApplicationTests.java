package com.connectcrew.teamone.chatservice;

import com.connectcrew.teamone.chatservice.chat.adapter.out.persistence.repository.ChatRepository;
import com.connectcrew.teamone.chatservice.chatroom.adapter.out.persistance.repository.ChatRoomRepository;
import com.connectcrew.teamone.chatservice.user.adapter.out.persistence.repository.UserRepository;
import com.mongodb.client.MongoClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;

@SpringBootTest
@EnableAutoConfiguration(exclude = {KafkaAutoConfiguration.class})
class ChatServiceApplicationTests {

	// redis beans
	@MockBean
	RedisConnectionFactory redisConnectionFactory;

	@MockBean
	RedisTemplate<String, Object> redisTemplate;

	@MockBean
	RedisMessageListenerContainer redisMessageListenerContainer;

	@MockBean
	RedisKeyValueAdapter redisKeyValueAdapter;

	// kafka beans
	@MockBean
	KafkaTemplate<String, String> kafkaTemplate;

	@MockBean
	ProducerFactory<String, String> producerFactory;

	@MockBean
	ConsumerFactory<String, String> consumerFactory;

	@MockBean
	ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory;

	@MockBean
	MessageListenerContainer messageListenerContainer;

	@MockBean
	KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

	// mongo beans
	@MockBean
	MongoTemplate mongoTemplate;

	@MockBean
	GridFsTemplate gridFsTemplate;

	@MockBean
	MongoClient mongoClient;

	@MockBean
	ChatRepository chatRepository;

	@MockBean
	ChatRoomRepository chatRoomRepository;

	@MockBean
	UserRepository userRepository;

	@Test
	void contextLoads() {
	}

}
