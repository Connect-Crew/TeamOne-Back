package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response;

public record ApplyStatusResponse(
        String partKey,
        String partDescription,
        String partCategoryKey,
        String partCategoryDescription,
        Long applies,
        Long current,
        Long max,
        String comment
) {
}
