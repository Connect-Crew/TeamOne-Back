package com.connectcrew.teamone.projectservice.member.application.port.in;

import com.connectcrew.teamone.projectservice.member.application.port.in.command.ApplyCommand;
import reactor.core.publisher.Mono;

public interface UpdateMemberUseCase {
    Mono<Boolean> apply(ApplyCommand command);
}
