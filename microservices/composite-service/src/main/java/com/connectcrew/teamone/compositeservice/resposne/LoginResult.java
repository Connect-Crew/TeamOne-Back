package com.connectcrew.teamone.compositeservice.resposne;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record LoginResult(
        Long id,
        String nickname,
        String profile,
        String introduction,
        Double temperature,
        Integer responseRate,
        List<String> parts,
        String email,
        String token,
        LocalDateTime exp,
        String refreshToken,
        LocalDateTime refreshExp
) {
}
