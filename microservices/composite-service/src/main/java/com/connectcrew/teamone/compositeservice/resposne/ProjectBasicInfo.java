package com.connectcrew.teamone.compositeservice.resposne;

import com.connectcrew.teamone.api.project.NameKey;
import com.connectcrew.teamone.api.project.NameKeyValue;
import com.connectcrew.teamone.api.project.values.MemberPart;
import com.connectcrew.teamone.api.project.values.ProjectCategory;
import com.connectcrew.teamone.api.project.values.Region;
import com.connectcrew.teamone.api.project.values.SkillType;

import java.util.Arrays;
import java.util.List;

public record ProjectBasicInfo(
        List<NameKey> region,
        List<NameKeyValue<NameKey>> job,
        List<String> skill,
        List<NameKey> category
) {

    public ProjectBasicInfo() {
        this(
                Region.getNameKeys(),
                MemberPart.getNameKeyValues(),
                Arrays.stream(SkillType.values()).map(Enum::name).toList(),
                ProjectCategory.getNameKeys()
        );
    }


}
