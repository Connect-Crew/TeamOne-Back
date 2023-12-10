package com.connectcrew.teamone.projectservice.project.adapter.out.persistence.repository;

import com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity.ProjectEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProjectRepository extends ReactiveCrudRepository<ProjectEntity, Long> {
}
