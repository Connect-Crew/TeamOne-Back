package com.connectcrew.teamone.compositeservice.composite.domain.vo;


import com.connectcrew.teamone.api.projectservice.enums.MemberPart;

public record CreateRecruitInfo(
        MemberPart part,
        String comment,
        Integer max
) {
}
