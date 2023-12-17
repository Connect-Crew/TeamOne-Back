package com.connectcrew.teamone.compositeservice.composite.domain;

import com.connectcrew.teamone.compositeservice.composite.domain.enums.*;

import javax.swing.plaf.synth.Region;
import java.util.List;

public record ProjectFilterOption(
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
}
