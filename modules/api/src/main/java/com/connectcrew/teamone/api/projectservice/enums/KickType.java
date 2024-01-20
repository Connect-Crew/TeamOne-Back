package com.connectcrew.teamone.api.projectservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KickType {
    ABUSE("욕설/비하발언"),
    BAD_PARTICIPATION("참여율 저조"),
    DISSENSION("팀원과의 불화"),
    GIVEN_UP("자진 중도 포기"),
    OBSCENITY("19+ 음란성, 만남 유도"),
    ETC("기타");

    private final String description;
}
