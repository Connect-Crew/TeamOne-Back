package com.connectcrew.teamone.api.user.auth.res;

import com.connectcrew.teamone.api.user.auth.Role;

public record UserRes(
        Long id,
        String username,
        String nickname,
        String password,
        String email,
        Role role,
        String createdDate,
        String modifiedDate
) {

}
