package com.connectcrew.teamone.userservice.entity;

import java.util.Set;

public record ChatCustomEntity(
        String chatRoomId,
        Set<Long> members
) {

}
