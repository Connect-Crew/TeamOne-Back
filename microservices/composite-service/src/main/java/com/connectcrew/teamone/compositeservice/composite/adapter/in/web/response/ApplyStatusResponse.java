package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response;

public record ApplyStatusResponse(
        Long applies,
        Long current,
        Long max
) {
}
