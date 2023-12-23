package com.connectcrew.teamone.projectservice.global.exceptions.application.port.in;

import com.connectcrew.teamone.projectservice.global.exceptions.enums.ErrorLevel;

public interface SendErrorNotificationUseCase {
    void send(String method, ErrorLevel level, Throwable ex);
}
