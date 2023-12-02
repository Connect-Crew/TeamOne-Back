package com.connectcrew.teamone.chatservice.chatroom.application;

import com.connectcrew.teamone.chatservice.chatroom.application.port.in.CreateChatRoomUseCase;
import com.connectcrew.teamone.chatservice.chatroom.application.port.in.QueryChatRoomUseCase;
import com.connectcrew.teamone.chatservice.chatroom.application.port.in.UpdateChatRoomUseCase;
import com.connectcrew.teamone.chatservice.chatroom.application.port.out.DeleteChatRoomOutput;
import com.connectcrew.teamone.chatservice.chatroom.application.port.out.FindChatRoomOutput;
import com.connectcrew.teamone.chatservice.chatroom.application.port.out.SaveChatRoomOutput;
import com.connectcrew.teamone.chatservice.chatroom.domain.ChatRoom;
import com.connectcrew.teamone.chatservice.chatroom.domain.enums.ChatRoomExceptionMessage;
import com.connectcrew.teamone.chatservice.chatroom.domain.exception.NotFoundChatRoomException;
import com.connectcrew.teamone.chatservice.user.application.port.in.UpdateUserUseCase;
import com.connectcrew.teamone.chatservice.user.domain.enums.UserExceptionMessage;
import com.connectcrew.teamone.chatservice.chatroom.domain.factory.ChatRoomFactory;
import com.connectcrew.teamone.chatservice.user.application.port.out.FindUserOutput;
import com.connectcrew.teamone.chatservice.user.application.port.out.SaveUserOutput;
import com.connectcrew.teamone.chatservice.user.domain.User;
import com.connectcrew.teamone.chatservice.user.domain.exceptions.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomAplService implements CreateChatRoomUseCase, QueryChatRoomUseCase, UpdateChatRoomUseCase {
    private final FindUserOutput findUserOutput;

    private final SaveUserOutput saveUserOutput;

    private final SaveChatRoomOutput saveChatRoomOutput;

    private final FindChatRoomOutput findChatRoomOutput;

    private final DeleteChatRoomOutput deleteChatRoomOutput;
    private final UpdateUserUseCase updateUserUseCase;

    @Override
    @Transactional
    public ChatRoom createChatRoom(Set<Long> userIds) {
        ChatRoom chatRoom = ChatRoomFactory.newInstance(userIds);
        UUID id = chatRoom.id();

        // TODO user 기능을 user usecase로 빼기
        List<User> users = findUserOutput.findAllByIds(userIds);
        HashSet<Long> userIdsSet = users.stream().map(User::id).collect(Collectors.toCollection(HashSet::new));
        for (Long userId : userIds) {
            if (userIdsSet.contains(userId)) continue;

            users.add(new User(userId, new HashSet<>()));
        }

        users.forEach(user -> user.addChatRoom(id));

        chatRoom = saveChatRoomOutput.saveChatRoom(chatRoom);
        updateUserUseCase.updateAll(users);

        return chatRoom;
    }

    @Override
    public Set<ChatRoom> findAllByUserId(Long userId) {
        Optional<User> user = findUserOutput.findById(userId);
        if (user.isEmpty()) return new HashSet<>();

        return findChatRoomOutput.findAllByIds(user.get().chatRooms());
    }

    @Override
    @Transactional
    public ChatRoom addMember(UUID roomId, Long userId) {
        ChatRoom chatRoom = findChatRoomOutput.findById(roomId).orElseThrow(() -> new NotFoundChatRoomException(ChatRoomExceptionMessage.CHAT_ROOM_NOT_FOUND.getMessage()));
        User user = findUserOutput.findById(userId).orElse(new User(userId, new HashSet<>())); // TODO : usecase로 수정하기

        chatRoom.addMember(userId);
        user.addChatRoom(roomId);

        chatRoom = saveChatRoomOutput.saveChatRoom(chatRoom);
        saveUserOutput.save(user); // TODO : usecase로 수정하기

        // TODO : Send Enter Message

        return chatRoom;
    }

    @Override
    @Transactional
    public ChatRoom removeMember(UUID roomId, Long userId) {
        ChatRoom chatRoom = findChatRoomOutput.findById(roomId).orElseThrow(() -> new NotFoundChatRoomException(ChatRoomExceptionMessage.CHAT_ROOM_NOT_FOUND.getMessage()));
        User user = findUserOutput.findById(userId).orElseThrow(() -> new NotFoundUserException(UserExceptionMessage.USER_NOT_FOUND.getMessage()));

        chatRoom.removeMember(userId);
        user.removeChatRoom(roomId);

        chatRoom = saveChatRoomOutput.saveChatRoom(chatRoom);
        saveUserOutput.save(user); // TODO : usecase로 수정하기

        // TODO : Send Exit Message

        return chatRoom;
    }
}
