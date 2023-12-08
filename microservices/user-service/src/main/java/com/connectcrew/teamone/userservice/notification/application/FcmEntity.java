package com.connectcrew.teamone.userservice.notification.application;

import com.connectcrew.teamone.userservice.notification.domain.FcmToken;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fcm")
public class FcmEntity {

    @Id
    private Long id;

    private Long userId;

    private String token;

    public static FcmEntity fromDomain(FcmToken fcmToken) {
        return FcmEntity.builder()
                .id(fcmToken.id())
                .userId(fcmToken.user())
                .token(fcmToken.token())
                .build();
    }

    public FcmToken toDomain() {
        return new FcmToken(id, userId, token);
    }
}
