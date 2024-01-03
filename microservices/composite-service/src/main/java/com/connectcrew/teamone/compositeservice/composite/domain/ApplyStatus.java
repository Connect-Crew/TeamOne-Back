package com.connectcrew.teamone.compositeservice.composite.domain;

import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response.ApplyStatusResponse;
import com.connectcrew.teamone.compositeservice.composite.domain.enums.MemberPart;

public record ApplyStatus(
        MemberPart part,
        Long applies,
        Long current,
        Long max
) {

    public ApplyStatusResponse toResponse() {
        return new ApplyStatusResponse(applies, current, max);
    }
}
