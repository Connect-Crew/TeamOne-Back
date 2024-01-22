package com.connectcrew.teamone.projectservice.member.application.port.in.command;

import com.connectcrew.teamone.api.projectservice.enums.KickType;
import com.connectcrew.teamone.api.projectservice.member.KickApiRequest;
import com.connectcrew.teamone.projectservice.member.domain.Kick;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record KickCommand(
        Long projectId,
        Long userId,
        String userNickname,
        Long leaderId,
        Map<KickType, String> typeMessageMap
) {

    public static KickCommand from(Long projectId, Long userId, String userNickname, Long leader, List<KickApiRequest> request) {
        return new KickCommand(
                projectId,
                userId,
                userNickname,
                leader,
                request.stream()
                        .collect(
                                Collectors.toMap(
                                        KickApiRequest::type,
                                        KickApiRequest::message
                                )
                        )
        );
    }

    public Kick toDomain() {
        return new Kick(
                projectId,
                userId,
                typeMessageMap
        );
    }
}
