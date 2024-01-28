package com.connectcrew.teamone.api.wish;

import java.time.LocalDateTime;

public record WishApiEvent(
        Long userId,
        String message,
        LocalDateTime createdAt
) {
}
