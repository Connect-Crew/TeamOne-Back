package com.connectcrew.teamone.api.projectservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectGoal {
    PORTFOLIO("포트폴리오"),
    STARTUP("예비창업");
    private final String description;
}