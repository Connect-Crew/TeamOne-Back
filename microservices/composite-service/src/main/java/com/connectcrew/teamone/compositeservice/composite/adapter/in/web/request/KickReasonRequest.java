package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request;

import com.connectcrew.teamone.api.projectservice.enums.KickType;

public record KickReasonRequest(
        KickType type,
        String reason
) {
}
