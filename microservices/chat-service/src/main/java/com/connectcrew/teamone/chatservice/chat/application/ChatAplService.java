package com.connectcrew.teamone.chatservice.chat.application;

import com.connectcrew.teamone.chatservice.chat.application.port.in.CreateChatUseCase;
import com.connectcrew.teamone.chatservice.chat.application.port.in.QueryChatUseCase;
import com.connectcrew.teamone.chatservice.chat.application.port.in.query.SearchChatPageQuery;
import com.connectcrew.teamone.chatservice.chat.application.port.out.FindChatOutput;
import com.connectcrew.teamone.chatservice.chat.application.port.out.PublishChatOutput;
import com.connectcrew.teamone.chatservice.chat.application.port.out.SaveChatOutput;
import com.connectcrew.teamone.chatservice.chat.domain.Chat;
import com.connectcrew.teamone.chatservice.chat.domain.enums.MessageType;
import com.connectcrew.teamone.chatservice.chatroom.domain.enums.ChatRoomExceptionMessage;
import com.connectcrew.teamone.chatservice.chatroom.domain.exception.NotRegisteredChatRoomException;
import com.connectcrew.teamone.chatservice.user.application.port.in.QueryUserUseCase;
import com.connectcrew.teamone.chatservice.user.application.port.in.UpdateUserUseCase;
import com.connectcrew.teamone.chatservice.user.domain.enums.UserExceptionMessage;
import com.connectcrew.teamone.chatservice.user.domain.exceptions.NotFoundUserException;
import com.connectcrew.teamone.chatservice.user.domain.vo.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatAplService implements QueryChatUseCase, CreateChatUseCase {

    private final FindChatOutput findChatOutput;
    private final UpdateUserUseCase updateUserUseCase;
    private final QueryUserUseCase findUserUseCase;
    private final SaveChatOutput saveChatOutput;
    private final PublishChatOutput publishChatOutput;

    @Override
    public List<Chat> searchChatPage(SearchChatPageQuery query) {
        return findChatOutput.findAllByRoomIdAndPageNumberAndPageSize(query.roomId(), query.page(), query.size());
    }

    @Override
    public void createChat(UUID roomId, Long senderId, String message, WebSocketSession session) {
        updateUserUseCase.updateUserSessionIfNotExists(senderId, session);

        UserSession userSession = findUserUseCase.findUserSessionByUserId(senderId)
                .orElseThrow(() -> new NotFoundUserException(UserExceptionMessage.USER_NOT_FOUND.getMessage()));

        if (!userSession.isJoined(roomId))
            throw new NotRegisteredChatRoomException(ChatRoomExceptionMessage.NOT_REGISTERED_CHAT_ROOM.getMessage());

        // TODO Factory로 빼기
        Chat chat = new Chat(MessageType.TALK, senderId, roomId, message, LocalDateTime.now());

        chat = saveChatOutput.save(chat);

        publishChatOutput.publish(chat);
    }
}
