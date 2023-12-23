package com.connectcrew.teamone.compositeservice.global.error.application.port.in;

import com.connectcrew.teamone.compositeservice.global.error.enums.ErrorLevel;

public interface SendErrorNotificationUseCase {
    void send(String method, ErrorLevel level, Throwable ex);
}
