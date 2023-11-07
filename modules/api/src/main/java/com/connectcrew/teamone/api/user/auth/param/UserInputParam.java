package com.connectcrew.teamone.api.user.auth.param;

import com.connectcrew.teamone.api.user.auth.Social;
import lombok.Builder;

@Builder
public record UserInputParam(
        String socialId,
        Social provider,
        String username,
        String nickname,
        String profile,
        String email,
        boolean termsAgreement,
        boolean privacyAgreement
) {
}
