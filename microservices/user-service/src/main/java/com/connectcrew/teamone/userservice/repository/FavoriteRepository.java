package com.connectcrew.teamone.userservice.repository;

import com.connectcrew.teamone.userservice.entity.FavoriteEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface FavoriteRepository extends ReactiveCrudRepository<FavoriteEntity, Long> {
}
