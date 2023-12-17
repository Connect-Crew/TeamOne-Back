package com.connectcrew.teamone.compositeservice.composite.domain.vo;

import java.util.List;

public record NameKeyValue<T>(
        String name,
        String key,
        List<T> value
) {
}
