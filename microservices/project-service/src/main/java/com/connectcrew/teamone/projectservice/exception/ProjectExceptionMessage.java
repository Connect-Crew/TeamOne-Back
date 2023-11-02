package com.connectcrew.teamone.projectservice.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProjectExceptionMessage {
    NOT_FOUND_PROJECT("프로젝트를 찾을수 없습니다."),
    INVALID_PROJECT_OWNER("프로젝트의 리더가 아닙니다."),
    TITLE_LENGTH_2_OVER("프로젝트 제목은 2자 이상이어야 합니다."),
    TITLE_LENGTH_30_UNDER("프로젝트 제목은 30자 이하여야 합니다."),
    BANNER_MAX_3("프로젝트 배너는 3개 이하여야 합니다."),
    ILLEGAL_BANNER_PATH("프로젝트 배너 경로가 올바르지 않습니다."),
    ILLEGAL_BANNER_NAME("프로젝트 배너 이름이 올바르지 않습니다."),
    ILLEGAL_BANNER_EXTENSION("프로젝트 배너 확장자가 올바르지 않습니다."),
    START_BEFORE_END("프로젝트 시작일은 종료일보다 빨라야 합니다."),
    CAREER_MIN_BEFORE_MAX("경력 최소값은 최대값보다 작아야 합니다."),
    CATEGORY_MIN_1("카테고리는 1개 이상이어야 합니다."),
    CATEGORY_MAX_3("카테고리는 3개 이하여야 합니다."),
    INTRODUCTION_LENGTH_1000_UNDER("프로젝트 소개는 1000자 이하여야 합니다."),
    RECRUIT_COMMENT_LENGTH_30_UNDER("모집 코멘트는 30자 이하여야 합니다."),
    RECRUIT_MAX_0_OVER("모집 인원은 0명 이상이어야 합니다."),
    RECRUIT_MAX_SUM_10_UNDER("최대 10명까지 모집 가능합니다."),
    CREATE_PROJECT_FAILED("프로젝트 생성에 실패했습니다."),
    LOAD_PROJECT_FAILED("프로젝트 조회에 실패했습니다."),
    ;

    private final String message;

    @Override
    public String toString() {
        return message;
    }
}
