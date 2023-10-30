package com.connectcrew.teamone.projectservice.repository;

import com.connectcrew.teamone.projectservice.entity.Part;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PartRepository extends ReactiveCrudRepository<Part, Long> {
}
