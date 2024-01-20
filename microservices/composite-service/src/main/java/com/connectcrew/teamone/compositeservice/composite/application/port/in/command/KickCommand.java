package com.connectcrew.teamone.compositeservice.composite.application.port.in.command;

import com.connectcrew.teamone.api.projectservice.enums.KickType;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request.KickReasonRequest;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request.KickRequest;
import com.connectcrew.teamone.compositeservice.composite.domain.Kick;

import java.util.Map;
import java.util.stream.Collectors;

public record KickCommand(
        Long project,
        Long leaderId,
        Long userId,
        String username,
        Map<KickType, String> reasonMap
) {

    public static KickCommand from(Long leaderId, String username, KickRequest request) {
        return new KickCommand(
                request.project(),
                leaderId,
                request.userId(),
                username,
                request.reasons().stream()
                        .collect(
                                Collectors.toMap(
                                        KickReasonRequest::type,
                                        KickReasonRequest::reason
                                )
                        )
        );
    }

    public Kick toDomain() {
        return new Kick(
                project,
                leaderId,
                userId,
                username,
                reasonMap
        );
    }
}
