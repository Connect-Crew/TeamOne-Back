package com.connectcrew.teamone.compositeservice.composite.application.port.out;


import com.connectcrew.teamone.compositeservice.composite.domain.FcmNotification;

public interface SendNotificationOutput {

    void send(FcmNotification notification);
}
