package com.connectcrew.teamone.compositeservice.global.error.domain;

import com.connectcrew.teamone.compositeservice.global.error.enums.ErrorLevel;
import lombok.Builder;

@Builder
public record ErrorNotification(
        ErrorLevel level,
        String service,
        String caller,
        String message
) {

}
