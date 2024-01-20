package com.connectcrew.teamone.projectservice.member.domain;

import com.connectcrew.teamone.api.projectservice.enums.KickType;
import com.connectcrew.teamone.projectservice.member.adapter.out.persistence.entity.KickEntity;

import java.util.List;
import java.util.Map;

public record Kick(
        Long memberId,
        Long userId,
        Map<KickType, String> typeMessageMap
) {

    public List<KickEntity> toEntities() {
        return typeMessageMap.entrySet().stream()
                .map(entry -> new KickEntity(
                        null,
                        memberId,
                        userId,
                        entry.getKey().name(),
                        entry.getValue()
                ))
                .toList();
    }
}
