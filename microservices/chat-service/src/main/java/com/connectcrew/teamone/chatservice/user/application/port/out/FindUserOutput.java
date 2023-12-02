package com.connectcrew.teamone.chatservice.user.application.port.out;

import com.connectcrew.teamone.chatservice.user.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FindUserOutput {
    Optional<User> findById(Long id);

    List<User> findAllByIds(Set<Long> ids);
}
