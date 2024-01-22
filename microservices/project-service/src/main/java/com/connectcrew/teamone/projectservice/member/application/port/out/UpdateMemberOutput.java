package com.connectcrew.teamone.projectservice.member.application.port.out;

import reactor.core.publisher.Mono;

import java.util.List;

public interface UpdateMemberOutput {
    Mono<Boolean> decreaseMemberCount(List<Long> partId);
}
