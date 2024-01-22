package com.connectcrew.teamone.userservice.profile.domain;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import lombok.Builder;

@Builder
public record Part(
        Long partId,
        Long profileId,
        MemberPart part
) {
}
