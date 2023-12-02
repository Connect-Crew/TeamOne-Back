package com.connectcrew.teamone.chatservice.user.adapter.out.persistence;

import com.connectcrew.teamone.chatservice.user.adapter.out.persistence.entity.UserEntity;
import com.connectcrew.teamone.chatservice.user.domain.User;
import com.connectcrew.teamone.chatservice.user.adapter.out.persistence.repository.UserRepository;
import com.connectcrew.teamone.chatservice.user.application.port.out.FindUserOutput;
import com.connectcrew.teamone.chatservice.user.application.port.out.SaveUserOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class UserPersistenceAdapter implements SaveUserOutput, FindUserOutput {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id).map(UserEntity::toDomain);
    }

    @Override
    public List<User> findAllByIds(Set<Long> ids) {
        return userRepository.findAllById(ids)
                .stream().map(UserEntity::toDomain)
                .toList();
    }

    @Override
    public User save(User user) {
        return userRepository.save(UserEntity.toEntity(user)).toDomain();
    }

    @Override
    public List<User> saveAll(List<User> users) {
        List<UserEntity> entities = users.stream().map(UserEntity::toEntity).toList();
        return userRepository.saveAll(entities)
                .stream().map(UserEntity::toDomain)
                .toList();
    }
}
