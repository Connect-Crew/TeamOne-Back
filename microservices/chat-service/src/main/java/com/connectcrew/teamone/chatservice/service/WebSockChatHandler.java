package com.connectcrew.teamone.chatservice.service;

import com.connectcrew.teamone.chatservice.model.ChatMessage;
import com.connectcrew.teamone.chatservice.model.MessageType;
import com.connectcrew.teamone.chatservice.model.User;
import com.connectcrew.teamone.chatservice.request.ProjectRequest;
import com.connectcrew.teamone.chatservice.request.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSockChatHandler extends TextWebSocketHandler {
    private final RedisMessagePublisher redisMessagePublisher;
    private final ObjectMapper objectMapper;
    private final ChatService chatService;
    private final UserRequest userRequest;
    private final ProjectRequest projectRequest;
    private final JwtValidator jwtValidator;

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.trace("chat payload: {}", payload);

        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);

        String token = chatMessage.token();
        if (!jwtValidator.validateToken(token)) {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(new ChatMessage(MessageType.ERROR, "", chatMessage.roomId(), "토큰이 유효하지 않습니다."))));
            return;
        }
        Long userId = jwtValidator.getId(token);
        String nickname = jwtValidator.getNickname(token);

        if (!chatService.existsUser(userId)) {
            Set<String> chatRooms = new HashSet<>();
            chatRooms.addAll(userRequest.getChatRooms(userId));
            chatRooms.addAll(projectRequest.getChatRooms(userId));

            User user = new User(userId, nickname, chatRooms, session);
            chatService.addUser(user);
        }

        if (!chatService.isJoinedChatRoom(userId, chatMessage.roomId())) {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(new ChatMessage(MessageType.ERROR, "", chatMessage.roomId(), "참여한 채팅방이 아닙니다."))));
            return;
        }

        // TODO 영속성 저장

        redisMessagePublisher.publish(chatMessage);

        // TODO push notification
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        chatService.removeUser(session);
    }
}
