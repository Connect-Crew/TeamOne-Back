package com.connectcrew.teamone.compositeservice.composite.domain;

import com.connectcrew.teamone.api.projectservice.enums.KickType;

import java.util.Map;

public record Kick(
        Long project,
        Long leaderId,
        Long userId,
        String username,
        Map<KickType, String> reasonMap
) {

}
