package com.connectcrew.teamone.chatservice.request;

import java.util.Set;

public interface ProjectRequest {
    Set<String> getChatRooms(Long userId);
}
