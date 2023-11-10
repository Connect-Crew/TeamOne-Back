package com.connectcrew.teamone.compositeservice.resposne;

import com.connectcrew.teamone.api.project.RecruitStatus;

public record RecruitStatusRes(
        String part,
        String comment,
        Integer current,
        Integer max
) {
    public RecruitStatusRes(RecruitStatus recruitStatus) {
        this(
                recruitStatus.part().getDescription(),
                recruitStatus.comment(),
                recruitStatus.current(),
                recruitStatus.max()
        );
    }
}
