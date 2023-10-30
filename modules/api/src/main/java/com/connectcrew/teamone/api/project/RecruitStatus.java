package com.connectcrew.teamone.api.project;

public record RecruitStatus(
        String part,
        String comment,
        Integer current,
        Integer max
) {
}
