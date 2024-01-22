package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response;


import com.connectcrew.teamone.compositeservice.composite.domain.RecruitStatus;

public record RecruitStatusResponse(
        Boolean containLeader,
        String category,
        String part,
        String partKey,
        String comment,
        Long current,
        Long max,
        Boolean applied
) {
    public RecruitStatusResponse(RecruitStatus recruitStatus, Boolean containLeader) {
        this(
                containLeader,
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
