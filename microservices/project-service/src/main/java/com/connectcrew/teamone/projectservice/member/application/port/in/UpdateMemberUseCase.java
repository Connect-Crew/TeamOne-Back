package com.connectcrew.teamone.projectservice.member.application.port.in;

import com.connectcrew.teamone.projectservice.member.application.port.in.command.UpdateMemberCommand;
import com.connectcrew.teamone.projectservice.member.domain.Apply;
import com.connectcrew.teamone.projectservice.member.domain.Member;
import reactor.core.publisher.Mono;

public interface UpdateMemberUseCase {

    Mono<Member> updateMember(UpdateMemberCommand command);

    Mono<Apply> accept(Long applyId, Long leaderId, String leaderMessage);

    Mono<Apply> reject(Long applyId, Long leaderId, String leaderMessage);
}
