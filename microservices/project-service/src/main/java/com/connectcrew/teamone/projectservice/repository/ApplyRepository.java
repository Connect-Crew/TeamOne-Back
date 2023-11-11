package com.connectcrew.teamone.projectservice.repository;

import com.connectcrew.teamone.projectservice.entity.Apply;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ApplyRepository extends ReactiveCrudRepository<Apply, Long> {
    Mono<Boolean> existsByPartIdAndUser(Long partId, Long user);
}
