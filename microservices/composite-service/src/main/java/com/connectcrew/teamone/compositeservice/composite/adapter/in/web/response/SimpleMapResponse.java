package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response;

import java.util.Map;

public record SimpleMapResponse <K, V> (
        Map<K, V> data
) {
}
