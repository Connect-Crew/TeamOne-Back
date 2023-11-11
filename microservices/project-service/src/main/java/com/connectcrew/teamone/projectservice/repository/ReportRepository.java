package com.connectcrew.teamone.projectservice.repository;

import com.connectcrew.teamone.projectservice.entity.Report;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ReportRepository extends ReactiveCrudRepository<Report, Long> {
    Mono<Boolean> existsByProjectAndUser(Long project, Long user);
}
