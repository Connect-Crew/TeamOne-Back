package com.connectcrew.teamone.userservice.user.domain;


import com.connectcrew.teamone.userservice.user.domain.enums.Role;
import com.connectcrew.teamone.userservice.user.domain.enums.Social;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record User(
        Long id,
        String socialId,
        Social provider,
        String username,
        String email,
        Role role,
        Boolean termsAgreement,
        Boolean privacyAgreement,
        LocalDateTime createdDate,
        LocalDateTime modifiedDate
) {

}