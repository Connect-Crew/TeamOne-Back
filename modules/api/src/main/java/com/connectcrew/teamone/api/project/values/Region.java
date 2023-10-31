package com.connectcrew.teamone.api.project.values;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Region {
    NONE("미설정"),
    SEOUL("서울"),
    GYEONGGI("경기"),
    INCHEON("인천"),
    KANGWON("강원"),
    BUSAN("부산"),
    DAEGU("대구"),
    GYEONGJU("경주"),
    KANGLEUNG("강릉"),
    ULSAN("울산"),
    GYEONGNAM("경남"),
    GYEONGBUK("경북"),
    GWANGJU("광주"),
    DAJEON("대전"),
    CHUNGNAM("충남"),
    CHUNGBUK("충북"),
    GEONNAM("전남"),
    GEONBUK("전북"),
    JEJU("제주");

    private final String description;

    @Override
    public String toString() {
        return description;
    }


}
