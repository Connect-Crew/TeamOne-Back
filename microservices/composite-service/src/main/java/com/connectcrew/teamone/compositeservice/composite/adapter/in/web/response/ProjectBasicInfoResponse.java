package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response;


import com.connectcrew.teamone.api.global.NameKey;
import com.connectcrew.teamone.api.global.NameKeyValue;
import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.enums.ProjectCategory;
import com.connectcrew.teamone.api.projectservice.enums.Region;
import com.connectcrew.teamone.compositeservice.global.enums.SkillType;

import java.util.Arrays;
import java.util.List;

public record ProjectBasicInfoResponse(
        List<NameKey> region,
        List<NameKeyValue<NameKey>> job,
        List<String> skill,
        List<NameKey> category
) {

    public ProjectBasicInfoResponse() {
        this(
                Region.getNameKeys(),
                MemberPart.getNameKeyValues(),
                Arrays.stream(SkillType.values()).map(Enum::name).toList(),
                ProjectCategory.getNameKeys()
        );
    }


}
