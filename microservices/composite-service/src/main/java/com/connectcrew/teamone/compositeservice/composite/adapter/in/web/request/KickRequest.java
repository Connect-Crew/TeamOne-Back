package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request;

import java.util.List;

public record KickRequest(
        Long project,
        Long userId,
        List<KickReasonRequest> reasons
) {
}
