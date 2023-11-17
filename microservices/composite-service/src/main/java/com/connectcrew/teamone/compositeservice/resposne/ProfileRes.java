package com.connectcrew.teamone.compositeservice.resposne;

import lombok.Builder;

import java.util.List;

@Builder
public record ProfileRes(
        Long id,
        String nickname,
        String profile,
        String introduction,
        Double temperature,
        Integer responseRate,
        List<String> parts,
        List<RepresentProjectRes> representProjects
) {

}
