package com.connectcrew.teamone.projectservice.member.adapter.out.persistence.repository;

import com.connectcrew.teamone.projectservice.member.adapter.out.persistence.entity.KickEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface KickRepository extends ReactiveCrudRepository<KickEntity, Long> {

}
