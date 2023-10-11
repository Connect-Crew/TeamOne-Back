package com.connectcrew.teamone.api.user.auth.res;

import com.connectcrew.teamone.api.user.auth.Role;
import com.connectcrew.teamone.api.user.auth.Social;

public record UserRes(
        Long id,
        String socialId,
        Social provider,
        String username,
        String nickname,
        String email,
        Role role,
        String createdDate,
        String modifiedDate
) {

}
