package com.connectcrew.teamone.api.project.values;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberPartCategory {
    DEVELOP("개발"),
    DESIGN("디자인"),
    MARKETING("마케팅"),
    MANAGER("기획"),
    SALES("영업"),
    CS("고객서비스"),
    SPECIALIST("전문직"),
    ENGINEER("엔지니어링"),
    MEDIA("미디어"),
    ETC("기타");

    private final String description;
}
