package com.connectcrew.teamone.api.projectservice.member;

import com.connectcrew.teamone.api.projectservice.enums.KickType;

public record KickApiRequest(
        KickType type,
        String message
) {
}
