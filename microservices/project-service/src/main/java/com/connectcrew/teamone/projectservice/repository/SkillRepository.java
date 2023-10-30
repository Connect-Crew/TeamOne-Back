package com.connectcrew.teamone.projectservice.repository;

import com.connectcrew.teamone.projectservice.entity.Skill;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SkillRepository extends ReactiveCrudRepository<Skill, Long> {
}
