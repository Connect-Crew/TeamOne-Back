package com.connectcrew.teamone.projectservice.repository;

import com.connectcrew.teamone.projectservice.entity.ProjectMember;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProjectMemberRepository extends ReactiveCrudRepository<ProjectMember, Long> {
}
