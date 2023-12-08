package com.connectcrew.teamone.api.chat;

import java.util.Set;

public record ChatInfo(
        String id,
        Set<Long> members
) {
}
