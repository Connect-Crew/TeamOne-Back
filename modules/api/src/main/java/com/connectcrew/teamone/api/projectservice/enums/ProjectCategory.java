package com.connectcrew.teamone.api.projectservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectCategory {
    IT("IT"),
    APP("앱서비스"),
    TRAVEL("여행"),
    ECOMMERCE("쇼핑·이커머스"),
    COMMUNITY("커뮤니티"),
    EDUCATION("교육"),
    HEALTH_LIFE("건강·생활"),
    BABY_PET("육아·반려동물"),
    LOVE("연애"),
    GAME("게임"),
    FOOD("요식업"),
    FINANCE("금융"),
    HOUSE("부동산·숙박"),
    AI("인공지능"),
    ETC("기타");

    private final String description;
}
