package com.connectcrew.teamone.userservice.profile.adapter.out.persistence.repository;

import com.connectcrew.teamone.userservice.profile.adapter.out.persistence.entity.ProfileEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProfileRepository extends ReactiveCrudRepository<ProfileEntity, Long> {

    Mono<ProfileEntity> findByUserId(Long userId);

    Mono<Boolean> existsByNickname(String nickname);
}
