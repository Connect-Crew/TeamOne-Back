package com.connectcrew.teamone.projectservice.notification.application.port;

import com.connectcrew.teamone.projectservice.notification.application.port.in.SendNotificationUseCase;
import com.connectcrew.teamone.projectservice.notification.application.port.out.SendNotificationOutput;
import com.connectcrew.teamone.projectservice.notification.domain.Notification;
import com.connectcrew.teamone.projectservice.project.application.port.out.FindProjectOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SendNotificationAplService implements SendNotificationUseCase {
    private final SendNotificationOutput sendNotificationOutput;
    private final FindProjectOutput findProjectOutput;


    @Override
    public Mono<Boolean> sendToLeader(Long projectId, String title, String body, String deepLink) {
        return findProjectOutput.findById(projectId)
                .doOnNext(project -> sendNotificationOutput.send(new Notification(project.leader(), title, body, deepLink)))
                .thenReturn(true);
    }
}
