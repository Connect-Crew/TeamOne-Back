package com.connectcrew.teamone.projectservice.member.application.port.out;

import reactor.core.publisher.Mono;

import java.util.List;

public interface DeleteMemberOutput {
    Mono<List<Long>> deleteMemberPartById(Long memberId);
}
