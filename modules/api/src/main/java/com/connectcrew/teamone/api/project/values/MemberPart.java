package com.connectcrew.teamone.api.project.values;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberPart {
    FRONTEND(MemberPartCategory.DEVELOP),
    IOS(MemberPartCategory.DEVELOP),
    ANDROID(MemberPartCategory.DEVELOP),
    BACKEND(MemberPartCategory.DEVELOP),
    PROJECT_MANAGER(MemberPartCategory.PLANNING),
    DESIGNER(MemberPartCategory.DESIGN),
    ;
    private final MemberPartCategory category;

}
