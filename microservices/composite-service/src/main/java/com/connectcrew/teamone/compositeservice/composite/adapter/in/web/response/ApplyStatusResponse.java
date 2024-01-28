package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response;

public record ApplyStatusResponse(
        String part,
        String partCategory,
        Long applies,
        Long current,
        Long max,
        String comment
) {
}
