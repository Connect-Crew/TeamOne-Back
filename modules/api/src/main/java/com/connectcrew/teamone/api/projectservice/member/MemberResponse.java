package com.connectcrew.teamone.api.projectservice.member;

import com.connectcrew.teamone.api.projectservice.enums.Part;

import java.util.List;

public record MemberResponse(
        Long userId,
        Boolean isLeader,
        List<Part> parts
) {
}
