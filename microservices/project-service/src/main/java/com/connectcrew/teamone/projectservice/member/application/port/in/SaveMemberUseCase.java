package com.connectcrew.teamone.projectservice.member.application.port.in;

import com.connectcrew.teamone.projectservice.member.application.port.in.command.ApplyCommand;
import com.connectcrew.teamone.projectservice.member.application.port.in.command.SaveMemberCommand;
import com.connectcrew.teamone.projectservice.member.domain.Apply;
import com.connectcrew.teamone.projectservice.member.domain.Member;
import reactor.core.publisher.Mono;

public interface SaveMemberUseCase {
    Mono<Member> saveMember(SaveMemberCommand command);

    Mono<Apply> apply(ApplyCommand command);
}
