package com.connectcrew.teamone.projectservice.global.exceptions.domain;

import com.connectcrew.teamone.projectservice.global.exceptions.enums.ErrorLevel;
import lombok.Builder;

@Builder
public record ErrorNotification(
        ErrorLevel level,
        String service,
        String caller,
        String message
) {

}
