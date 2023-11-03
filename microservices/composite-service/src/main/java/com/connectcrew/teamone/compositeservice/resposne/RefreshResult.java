package com.connectcrew.teamone.compositeservice.resposne;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RefreshResult(
        String token,
        LocalDateTime exp
) {
}
