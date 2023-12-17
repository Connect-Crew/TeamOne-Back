package com.connectcrew.teamone.compositeservice.global.enums;

import com.connectcrew.teamone.compositeservice.composite.domain.vo.NameKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
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

    public static List<NameKey> getNameKeys() {
        return Arrays.stream(Region.values()).map(r -> new NameKey(r.description, r.name())).toList();
    }
}
