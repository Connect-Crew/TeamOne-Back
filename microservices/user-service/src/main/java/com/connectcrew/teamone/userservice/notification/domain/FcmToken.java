package com.connectcrew.teamone.userservice.notification.domain;

import lombok.Builder;

@Builder
public record FcmToken(
        Long id,
        Long user,
        String token
) {

}
