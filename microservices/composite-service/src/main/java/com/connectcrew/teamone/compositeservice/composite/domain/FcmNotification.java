package com.connectcrew.teamone.compositeservice.composite.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FcmNotification {
    private Long userId;
    private String title;
    private String body;
//    private String image;
//    private Map<String, String> data;

    @Builder
    public FcmNotification(Long userId, String title, String body) {
        this.userId = userId;
        this.title = title;
        this.body = body;
        // this.image = image;
        // this.data = data;
    }

    @Override
    public String toString() {
        return String.format("FcmNotification{userId=%d, title='%s', body='%s'}", userId, title, body);
    }
}
