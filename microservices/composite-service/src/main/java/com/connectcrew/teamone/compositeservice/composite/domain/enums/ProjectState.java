package com.connectcrew.teamone.compositeservice.composite.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectState {
    RECRUITING("모집중"),
    RECRUIT_END("모집마감"),
    PROCEEDING("진행중"),
    END("종료");

    private final String description;

    @Override
    public String toString() {
        return description;
    }
}
