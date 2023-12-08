package com.connectcrew.teamone.userservice.user.application.in;

import com.connectcrew.teamone.userservice.user.domain.User;
import com.connectcrew.teamone.userservice.user.application.in.command.CreateUserCommand;
import reactor.core.publisher.Mono;

public interface CreateUserUseCase {
    Mono<User> create(CreateUserCommand createUserCommand);
}
