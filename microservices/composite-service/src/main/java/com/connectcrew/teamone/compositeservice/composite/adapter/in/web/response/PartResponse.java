package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response;

import com.connectcrew.teamone.compositeservice.composite.domain.enums.MemberPart;

public record PartResponse(
        String key,
        String part,
        String category
) {
    public PartResponse(MemberPart part) {
        this(part.name(), part.getDescription(), part.getCategory().getDescription());
    }
}
