package com.connectcrew.teamone.projectservice.repository;

import com.connectcrew.teamone.projectservice.entity.ProjectCategory;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProjectCategoryRepository extends ReactiveCrudRepository<ProjectCategory, Long> {
}
