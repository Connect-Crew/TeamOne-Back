package com.connectcrew.teamone.userservice.notification.application.port.in;

import com.connectcrew.teamone.userservice.notification.domain.ErrorLevel;

public interface SendErrorNotificationUseCase {
    void send(String method, ErrorLevel level, Throwable ex);
}
