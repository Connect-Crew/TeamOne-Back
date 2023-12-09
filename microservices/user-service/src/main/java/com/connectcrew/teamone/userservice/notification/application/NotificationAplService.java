package com.connectcrew.teamone.userservice.notification.application;

import com.connectcrew.teamone.userservice.notification.application.port.in.SaveFcmTokenUseCase;
import com.connectcrew.teamone.userservice.notification.application.port.in.SendMessageUseCase;
import com.connectcrew.teamone.userservice.notification.application.port.in.command.SendMessageCommand;
import com.connectcrew.teamone.userservice.notification.application.port.out.DeleteFcmOutput;
import com.connectcrew.teamone.userservice.notification.application.port.out.FindFcmOutput;
import com.connectcrew.teamone.userservice.notification.application.port.out.SaveFcmOutput;
import com.connectcrew.teamone.userservice.notification.application.port.out.SendMessageOutput;
import com.connectcrew.teamone.userservice.notification.domain.FcmMessage;
import com.connectcrew.teamone.userservice.notification.domain.FcmToken;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationAplService implements SendMessageUseCase, SaveFcmTokenUseCase {
    private final FindFcmOutput findFcmOutput;
    private final DeleteFcmOutput deleteFcmOutput;
    private final SendMessageOutput sendMessageOutput;
    private final SaveFcmOutput saveFcmOutput;

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
    public Mono<Boolean> saveFcmToken(Long user, String token) {
        return saveFcmOutput.save(FcmToken.builder().user(user).token(token).build())
                .thenReturn(true);
    }
}
