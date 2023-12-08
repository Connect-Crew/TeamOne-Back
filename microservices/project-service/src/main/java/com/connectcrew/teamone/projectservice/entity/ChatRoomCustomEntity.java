package com.connectcrew.teamone.projectservice.entity;

import java.util.Set;

public record ChatRoomCustomEntity(
        String id,
        Set<Long> members
) {
}
