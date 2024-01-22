package com.connectcrew.teamone.api.userservice.notification.error;

import lombok.Builder;

@Builder
public record ErrorNotification(
        ErrorLevel level,
        String service,
        String caller,
        String message
) {

}

