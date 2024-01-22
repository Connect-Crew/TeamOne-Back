package com.connectcrew.teamone.compositeservice.composite.domain;


import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.member.MemberApiResponse;

import java.util.List;

public record ProjectMember(
        Long userId,
        Boolean isLeader,
        List<MemberPart> parts
) {
    public static ProjectMember of(MemberApiResponse res) {
        return new ProjectMember(
                res.userId(),
                res.isLeader(),
                res.parts()
        );
    }
}
