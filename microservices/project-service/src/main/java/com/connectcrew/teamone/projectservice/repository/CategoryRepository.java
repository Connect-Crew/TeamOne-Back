package com.connectcrew.teamone.projectservice.repository;

import com.connectcrew.teamone.projectservice.entity.Category;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CategoryRepository extends ReactiveCrudRepository<Category, Long> {
    Flux<Category> findAllByProject(Long project);
}
