package com.connectcrew.teamone.compositeservice.composite.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Career {
    NONE("경력무관", -1),
    SEEKER("준비생", 0),
    ENTRY("신입", 1),
    YEAR_1("1년", 2),
    YEAR_2("2년", 3),
    YEAR_3("3년", 4),
    YEAR_4("4년", 5),
    YEAR_5("5년", 6),
    YEAR_6("6년", 7),
    YEAR_7("7년", 8),
    YEAR_8("8년", 9),
    YEAR_9("9년", 10),
    YEAR_10_PLUS("10년+", 11);

    private final String description;
    private final Integer id;


    public static Career valueOf(Integer id) {
        for (Career career : Career.values()) {
            if (career.getId().equals(id)) {
                return career;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return description;
    }
}
