package com.connectcrew.teamone.api.project.values;

import com.connectcrew.teamone.api.project.NameKey;
import com.connectcrew.teamone.api.project.NameKeyValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum MemberPart {
    FRONTEND(MemberPartCategory.DEVELOP, "프론트엔드"),
    IOS(MemberPartCategory.DEVELOP, "IOS"),
    ANDROID(MemberPartCategory.DEVELOP, "안드로이드"),
    BACKEND(MemberPartCategory.DEVELOP, "백엔드"),
    PROJECT_MANAGER(MemberPartCategory.PLANNING, "프로젝트 매니저"),
    PLANNER(MemberPartCategory.PLANNING, "기획"),
    DESIGNER(MemberPartCategory.DESIGN, "디자이너"),
    ;
    private final MemberPartCategory category;
    private final String description;

    public static List<NameKeyValue<NameKey>> getNameKeyValues() {
        List<NameKeyValue<NameKey>> result = new ArrayList<>();

        Map<MemberPartCategory, List<NameKey>> partMap = new HashMap<>();
        for (MemberPart part : MemberPart.values()) {
            partMap.computeIfAbsent(part.category, k -> new ArrayList<>()).add(new NameKey(part.description, part.name()));
        }

        for (MemberPartCategory category : partMap.keySet()) {
            result.add(new NameKeyValue<>(category.getDescription(), category.name(), partMap.get(category)));
        }

        return result;
    }
}
