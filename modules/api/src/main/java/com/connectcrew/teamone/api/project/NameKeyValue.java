package com.connectcrew.teamone.api.project;

import java.util.List;

public record NameKeyValue<T>(
        String name,
        String key,
        List<T> value
) {
}
