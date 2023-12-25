package com.connectcrew.teamone.api.notification.error;

import lombok.Builder;

@Builder
public record ErrorNotification(
        ErrorLevel level,
        String service,
        String caller,
        String message
) {

}

