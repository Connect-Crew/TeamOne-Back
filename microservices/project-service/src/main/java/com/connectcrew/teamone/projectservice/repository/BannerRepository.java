package com.connectcrew.teamone.projectservice.repository;

import com.connectcrew.teamone.projectservice.entity.Banner;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BannerRepository extends ReactiveCrudRepository<Banner, Long> {
}
