package com.connectcrew.teamone.api.projectservice.leader;

public record ApplyStatusResponse(
        int applies,
        int current,
        int max
) {

}
