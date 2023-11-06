package com.connectcrew.teamone.projectservice.repository;

import com.connectcrew.teamone.projectservice.entity.Banner;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BannerRepository extends ReactiveCrudRepository<Banner, Long> {
    Flux<Banner> findAllByProject(Long project);

    Mono<Banner> findByProjectOrderByIdx(Long project);
}
