package com.connectcrew.teamone.chatservice.request;

import com.connectcrew.teamone.chatservice.model.ChatRoom;

import java.util.Set;

public interface ProjectRequest {
    Set<ChatRoom> getChatRooms(Long userId);
}
