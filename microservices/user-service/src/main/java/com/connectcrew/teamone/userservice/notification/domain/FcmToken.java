package com.connectcrew.teamone.userservice.notification.domain;

public record FcmToken(
        Long id,
        Long user,
        String token
) {

}
