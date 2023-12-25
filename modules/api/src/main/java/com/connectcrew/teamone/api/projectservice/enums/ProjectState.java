package com.connectcrew.teamone.api.projectservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectState {
    NOT_STARTED("진행 전"),
    IN_PROGRESS("진행 후"),
    COMPLETED("종료"),
    DELETED("삭제");

    private final String description;

    @Override
    public String toString() {
        return description;
    }
}
