package com.connectcrew.teamone.chatservice.model;

import java.util.Set;

public record ChatRoom(
        String id,
        Set<Long> members
) {

}
