package com.connectcrew.teamone.api.project;

import java.util.List;

public record ProjectMember(
        Long memberId,
        List<String> parts
) {
}
