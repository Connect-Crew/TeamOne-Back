package com.connectcrew.teamone.userservice.repository;

import com.connectcrew.teamone.userservice.entity.ChatCustomEntity;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ChatRepositoryImpl implements ChatRepository {

    private final DatabaseClient dc;

    public ChatRepositoryImpl(DatabaseClient dc) {
        this.dc = dc;
    }

    @Override
    public Flux<ChatCustomEntity> findAllChatIdAndMembersByUserId(Long userId) {
        return dc.sql("SELECT chatroom_id, user_id FROM chatroom_users WHERE chatroom_id IN (SELECT chatroom_id FROM chatroom_users WHERE user_id = :user_id) AND user_id <> :user_id;")
                .bind("user_id", userId)
                .map((row, rowMetadata) -> Tuples.of(Objects.requireNonNull(row.get("chatroom_id", String.class)), Objects.requireNonNull(row.get("user_id", Long.class))))
                .all()
                .groupBy(Tuple2::getT1)
                .flatMap(group -> group.collectList().map(list -> new ChatCustomEntity(group.key(), list.stream().map(Tuple2::getT2).collect(Collectors.toSet()))));

    }
}
