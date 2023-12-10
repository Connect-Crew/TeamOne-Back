package com.connectcrew.teamone.projectservice.project.adapter.out.persistence.repository;

import com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity.CategoryEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CategoryRepository extends ReactiveCrudRepository<CategoryEntity, Long> {
    Flux<CategoryEntity> findAllByProject(Long project);
}
