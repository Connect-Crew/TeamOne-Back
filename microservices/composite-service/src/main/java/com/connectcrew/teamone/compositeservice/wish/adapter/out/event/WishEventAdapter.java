package com.connectcrew.teamone.compositeservice.wish.adapter.out.event;

import com.connectcrew.teamone.api.constants.KafkaEventTopic;
import com.connectcrew.teamone.compositeservice.wish.application.port.out.SendWishMessageOutput;
import com.connectcrew.teamone.compositeservice.wish.domain.Wish;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class WishEventAdapter implements SendWishMessageOutput {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public Wish send(Wish wish) {
        log.info("send report: {}", wish);
        try {
            String body = objectMapper.writeValueAsString(wish.toEvent());
            kafkaTemplate.send(KafkaEventTopic.WishNotification, body);
            return wish;
        } catch (Exception e) {
            log.error("send wish - topic: {}, body: {}", KafkaEventTopic.WishNotification, wish, e);
            throw new RuntimeException("의견 전송에 실패했습니다! 다시 시도해주세요!");
        }
    }
}
