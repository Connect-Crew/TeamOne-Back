package com.connectcrew.teamone.chatservice.request;

import java.util.Set;

public interface UserRequest {
    Set<String> getChatRooms(Long userId);
}
