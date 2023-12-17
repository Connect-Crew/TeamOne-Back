package com.connectcrew.teamone.userservice.profile.domain;

import lombok.Builder;

@Builder
public record Profile(
        Long id,
        Long userId,
        String nickname,
        String profile,
        String introduction,
        Double temperature,
        Integer recvApply,
        Integer resApply
) {

    public Integer responseRate() {
        return recvApply == 0 ? 0 : (int) (((double) resApply / recvApply) * 100);
    }
}
