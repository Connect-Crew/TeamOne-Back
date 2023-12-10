package com.connectcrew.teamone.projectservice.project.application.port.in.query;

import com.connectcrew.teamone.api.project.values.*;
import com.connectcrew.teamone.projectservice.project.domain.vo.ProjectOption;
import lombok.Builder;

import java.util.List;

@Builder
public record ProjectQuery(
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

    public ProjectOption toOption() {
        return new ProjectOption(
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
