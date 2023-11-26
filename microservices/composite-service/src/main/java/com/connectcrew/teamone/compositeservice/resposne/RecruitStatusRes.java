package com.connectcrew.teamone.compositeservice.resposne;

import com.connectcrew.teamone.api.project.RecruitStatus;

public record RecruitStatusRes(
        String category,
        String part,
        String partKey,
        String comment,
        Integer current,
        Integer max,
        Boolean applied
) {
    public RecruitStatusRes(RecruitStatus recruitStatus) {
        this(
                recruitStatus.part().getCategory().getDescription(),
                recruitStatus.part().getDescription(),
                recruitStatus.part().name(),
                recruitStatus.comment(),
                recruitStatus.current(),
                recruitStatus.max(),
                recruitStatus.applied()
        );
    }
}
