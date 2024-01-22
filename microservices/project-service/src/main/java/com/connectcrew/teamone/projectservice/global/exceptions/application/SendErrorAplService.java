package com.connectcrew.teamone.projectservice.global.exceptions.application;

import com.connectcrew.teamone.api.userservice.notification.error.ErrorLevel;
import com.connectcrew.teamone.api.userservice.notification.error.ErrorNotification;
import com.connectcrew.teamone.projectservice.global.exceptions.application.port.in.SendErrorNotificationUseCase;
import com.connectcrew.teamone.projectservice.global.exceptions.application.port.out.SendErrorNotificationOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendErrorAplService implements SendErrorNotificationUseCase {
    private static final String SERVICE_NAME = "project-service";

    private final SendErrorNotificationOutput sendErrorNotificationOutput;

    @Override
    public void send(String method, ErrorLevel level, Throwable ex) {

        ErrorNotification notification = ErrorNotification.builder()
                .service(SERVICE_NAME)
                .caller(method)
                .level(level)
                .message(getExceptionMessages((Exception) ex))
                .build();
        sendErrorNotificationOutput.send(notification);
    }

    private String getExceptionMessages(Exception ex) {
        List<String> causeMessages =  new ArrayList<>();

        while (ex != null) {
            causeMessages.add(ex.getClass().getSimpleName() + " : " + ex.getMessage());
            ex = (Exception) ex.getCause();
        }

        return String.join("\n\tCause By ", causeMessages);
    }
}
