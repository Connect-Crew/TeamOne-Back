package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response;

import com.connectcrew.teamone.compositeservice.composite.domain.vo.LoginResult;

import java.time.OffsetDateTime;
import java.util.List;

public record LoginResponse(
        Long id,
        String nickname,
        String profile,
        String introduction,
        Double temperature,
        Integer responseRate,
        List<String> parts,
        String email,
        String token,
        OffsetDateTime exp,
        String refreshToken,
        OffsetDateTime refreshExp
) {

    public static LoginResponse from(LoginResult result) {
        return new LoginResponse(
                result.id(),
                result.nickname(),
                result.profile(),
                result.introduction(),
                result.temperature(),
                result.responseRate(),
                result.parts(),
                result.email(),
                result.token(),
                result.exp(),
                result.refreshToken(),
                result.refreshExp()
        );
    }
}
