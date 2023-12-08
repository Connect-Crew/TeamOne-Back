package com.connectcrew.teamone.chatservice.chat.application.port.in;

import com.connectcrew.teamone.chatservice.chat.application.port.in.query.SearchChatPageQuery;
import com.connectcrew.teamone.chatservice.chat.domain.Chat;

import java.util.List;

public interface QueryChatUseCase {
    List<Chat> searchChatPage(SearchChatPageQuery query);
}
