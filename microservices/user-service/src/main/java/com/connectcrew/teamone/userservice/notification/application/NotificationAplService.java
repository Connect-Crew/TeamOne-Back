package com.connectcrew.teamone.userservice.notification.application;

import com.connectcrew.teamone.api.notification.error.ErrorNotification;
import com.connectcrew.teamone.userservice.notification.application.port.in.SaveFcmTokenUseCase;
import com.connectcrew.teamone.userservice.notification.application.port.in.SendErrorNotificationUseCase;
import com.connectcrew.teamone.userservice.notification.application.port.in.SendMessageUseCase;
import com.connectcrew.teamone.userservice.notification.application.port.in.command.DiscordMessageCommand;
import com.connectcrew.teamone.userservice.notification.application.port.in.command.SendMessageCommand;
import com.connectcrew.teamone.userservice.notification.application.port.out.*;
import com.connectcrew.teamone.api.notification.error.ErrorLevel;
import com.connectcrew.teamone.userservice.notification.domain.FcmMessage;
import com.connectcrew.teamone.userservice.notification.domain.FcmToken;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationAplService implements SendMessageUseCase, SaveFcmTokenUseCase, SendErrorNotificationUseCase {
    private static final String SERVICE_NAME = "user-service";
    private final FindFcmOutput findFcmOutput;
    private final DeleteFcmOutput deleteFcmOutput;
    private final SendMessageOutput sendMessageOutput;
    private final SaveFcmOutput saveFcmOutput;
    private final SendDiscordOutput sendDiscordOutput;

    @Override
    public Mono<Boolean> sendMessage(SendMessageCommand command) {
        return findFcmOutput.findAllFcmTokenByUserId(command.userId())
                .flatMap(fcmToken -> {
                    try {
                        FcmMessage msg = command.toMessage(fcmToken.token());

                        return Mono.just(sendMessageOutput.sendMessage(msg));
                    } catch (FirebaseMessagingException ex) {
                        log.trace("send message - error: {}", ex.getMessage(), ex);
                        return deleteFcmOutput.deleteFcmToken(fcmToken);
                    }
                })
                .then()
                .thenReturn(true);
    }

    @Override
    public Mono<Boolean> sendMessage(DiscordMessageCommand command) {
        return Mono.just(sendDiscordOutput.sendMessage(command.toDomain()))
                .thenReturn(true);
    }

    @Override
    public Mono<Boolean> saveFcmToken(Long user, String token) {
        return saveFcmOutput.save(FcmToken.builder().user(user).token(token).build())
                .thenReturn(true);
    }

    @Override
    public void send(String method, ErrorLevel level, Throwable ex) {
        ErrorNotification notification = ErrorNotification.builder()
                .service(SERVICE_NAME)
                .caller(method)
                .level(level)
                .message(getExceptionMessages((Exception) ex))
                .build();

        sendDiscordOutput.sendMessage(DiscordMessageCommand.from(notification).toDomain());
    }

    private String getExceptionMessages(Exception ex) {
        List<String> causeMessages = new ArrayList<>();

        while (ex != null) {
            causeMessages.add(ex.getClass().getSimpleName() + " : " + ex.getMessage());
            ex = (Exception) ex.getCause();
        }

        return String.join("\n\tCause By ", causeMessages);
    }
}
