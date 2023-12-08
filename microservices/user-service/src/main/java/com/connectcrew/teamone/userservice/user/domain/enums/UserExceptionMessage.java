package com.connectcrew.teamone.userservice.user.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserExceptionMessage {
    TERMS_AGREEMENT_REQUIRED("서비스 이용약관에 동의해주세요."),
    PRIVACY_AGREEMENT_REQUIRED("개인정보 처리방침에 동의해주세요."),
    NAME_LENGTH_LESS_2("이름은 2자 이상 입력해주세요."),
    NAME_LENGTH_OVER_10("이름은 10자 이하로 입력해주세요."),
    SPACE_OR_SPECIAL_CHARACTER_IN_NAME("공백과 특수문자는 들어갈 수 없어요."),
    DUPLICATE_NICKNAME("이미 존재하는 닉네임입니다."),
    ALREADY_EXISTS_USER("이미 존재하는 유저입니다."),
    NOTFOUND_USER("사용자를 찾을 수 없습니다."),
    ;
    private final String message;

}
