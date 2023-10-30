package com.connectcrew.teamone.api.project.values;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProjectGoal {
    PORTFOLIO("포트폴리오"),
    STARTUP("예비창업");
    private final String description;

    @Override
    public String toString() {
        return description;
    }
}
