package com.connectcrew.teamone.compositeservice.composite.application.port.in.query;

import com.connectcrew.teamone.compositeservice.composite.domain.ProjectFilterOption;
import com.connectcrew.teamone.compositeservice.composite.domain.enums.*;
import com.connectcrew.teamone.compositeservice.global.enums.Region;

import java.util.List;

public record FindProjectListQuery(
        Integer lastId,
        int size,
        ProjectGoal goal,
        Career career,
        List<Region> region,
        Boolean online,
        MemberPart part,
        List<String> skills,
        List<ProjectState> states,
        List<ProjectCategory> category,
        String search
) {
    public ProjectFilterOption toDomain() {
        return new ProjectFilterOption(
                lastId,
                size,
                goal,
                career,
                region,
                online,
                part,
                skills,
                states,
                category,
                search
        );
    }
}
