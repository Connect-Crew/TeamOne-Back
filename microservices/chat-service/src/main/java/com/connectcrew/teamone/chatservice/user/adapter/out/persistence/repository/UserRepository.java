package com.connectcrew.teamone.chatservice.user.adapter.out.persistence.repository;

import com.connectcrew.teamone.chatservice.user.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity, Long> {
}
