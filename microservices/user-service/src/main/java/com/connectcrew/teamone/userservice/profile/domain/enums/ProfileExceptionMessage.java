package com.connectcrew.teamone.userservice.profile.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProfileExceptionMessage {
    NOTFOUND_PROFILE("프로필을 찾지 못했습니다.")
    ;
    private final String message;
}
