package com.connectcrew.teamone.chatservice.global.config;

import com.connectcrew.teamone.chatservice.chat.adapter.in.event.MemberModifiedEventChatListener;
import com.connectcrew.teamone.chatservice.chat.adapter.in.event.ChatMessageListener;
import com.connectcrew.teamone.chatservice.chat.adapter.in.web.response.ChatResponse;
import com.connectcrew.teamone.chatservice.global.constants.RedisTopic;
import com.connectcrew.teamone.chatservice.user.adapter.in.event.ChatRoomMemberModifiedEventListener;
import com.connectcrew.teamone.chatservice.user.adapter.in.event.UserSessionUpdateEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory(@Value("${spring.redis.host}") String host, @Value("${spring.redis.port}") int port) {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatResponse.class));
        return redisTemplate;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory,
                                                                       ChatMessageListener chatMessageListener,
                                                                       UserSessionUpdateEventListener userSessionUpdateEventListener,
                                                                       ChatRoomMemberModifiedEventListener chatRoomMemberModifiedEventListener,
                                                                       MemberModifiedEventChatListener memberModifiedEventChatListener
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(chatMessageListener, new ChannelTopic(RedisTopic.CHAT.getTopic()));
        container.addMessageListener(userSessionUpdateEventListener, new ChannelTopic(RedisTopic.CHAT_ROOM_CREATED.getTopic()));
        container.addMessageListener(chatRoomMemberModifiedEventListener, new ChannelTopic(RedisTopic.MEMBER_MODIFIED.getTopic()));
        container.addMessageListener(memberModifiedEventChatListener, new ChannelTopic(RedisTopic.MEMBER_MODIFIED.getTopic()));
        return container;
    }
}
