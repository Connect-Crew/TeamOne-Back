package com.connectcrew.teamone.chatservice.chat.application.port.out;

import com.connectcrew.teamone.chatservice.chat.domain.Chat;

import java.util.List;

public interface FindChatOutput {
    List<Chat> findAllByRoomIdAndPageNumberAndPageSize(String roomId, int pageNumber, int pageSize);
}
