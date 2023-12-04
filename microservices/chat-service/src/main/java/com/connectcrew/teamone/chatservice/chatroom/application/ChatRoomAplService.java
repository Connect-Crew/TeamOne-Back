package com.connectcrew.teamone.chatservice.chatroom.application;

import com.connectcrew.teamone.chatservice.chatroom.application.port.in.CreateChatRoomUseCase;
import com.connectcrew.teamone.chatservice.chatroom.application.port.in.QueryChatRoomUseCase;
import com.connectcrew.teamone.chatservice.chatroom.application.port.in.UpdateChatRoomUseCase;
import com.connectcrew.teamone.chatservice.chatroom.application.port.out.*;
import com.connectcrew.teamone.chatservice.chatroom.domain.ChatRoom;
import com.connectcrew.teamone.chatservice.chatroom.domain.enums.ChatRoomExceptionMessage;
import com.connectcrew.teamone.chatservice.chatroom.domain.enums.ChatRoomType;
import com.connectcrew.teamone.chatservice.chatroom.domain.enums.MemberModifiedType;
import com.connectcrew.teamone.chatservice.chatroom.domain.exception.NotFoundChatRoomException;
import com.connectcrew.teamone.chatservice.chatroom.domain.factory.ChatRoomFactory;
import com.connectcrew.teamone.chatservice.user.application.port.in.UpdateUserUseCase;
import com.connectcrew.teamone.chatservice.user.application.port.out.FindUserOutput;
import com.connectcrew.teamone.chatservice.user.application.port.out.SaveUserOutput;
import com.connectcrew.teamone.chatservice.user.domain.User;
import com.connectcrew.teamone.chatservice.user.domain.enums.UserExceptionMessage;
import com.connectcrew.teamone.chatservice.user.domain.exceptions.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomAplService implements CreateChatRoomUseCase, QueryChatRoomUseCase, UpdateChatRoomUseCase {
    private final FindUserOutput findUserOutput;

    private final SaveUserOutput saveUserOutput;

    private final SaveChatRoomOutput saveChatRoomOutput;

    private final FindChatRoomOutput findChatRoomOutput;

    private final DeleteChatRoomOutput deleteChatRoomOutput;
    private final UpdateUserUseCase updateUserUseCase;

    private final CreateChatRoomEventOutput createChatRoomEventOutput;
    private final ModifiedMemberChatRoomEventOutput modifiedMemberChatRoomEventOutput;

    @Override
    @Transactional
    public ChatRoom createChatRoom(ChatRoomType type, Set<Long> userIds) {
        ChatRoom chatRoom = ChatRoomFactory.newInstance(type, userIds);
        UUID id = chatRoom.id();

        updateUserUseCase.addUsersChatRoomJoin(id, userIds);

        chatRoom = saveChatRoomOutput.saveChatRoom(chatRoom);

        createChatRoomEventOutput.publish(chatRoom);

        return chatRoom;
    }

    @Override
    public Set<ChatRoom> findAllByUserId(Long userId) {
        Optional<User> user = findUserOutput.findById(userId);
        if (user.isEmpty()) return new HashSet<>();

        return findChatRoomOutput.findAllByIds(user.get().chatRooms());
    }

    @Override
    public Optional<ChatRoom> findByRoomId(UUID roomId) {
        return findChatRoomOutput.findById(roomId);
    }

    @Override
    @Transactional
    public ChatRoom addMember(UUID roomId, Long userId) {
        ChatRoom chatRoom = findChatRoomOutput.findById(roomId).orElseThrow(() -> new NotFoundChatRoomException(ChatRoomExceptionMessage.CHAT_ROOM_NOT_FOUND.getMessage()));
        chatRoom.addMember(userId);

        User user = findUserOutput.findById(userId).orElse(new User(userId, new HashSet<>()));
        user.addChatRoom(roomId);

        chatRoom = saveChatRoomOutput.saveChatRoom(chatRoom);
        saveUserOutput.save(user);

        modifiedMemberChatRoomEventOutput.publishModifiedMember(roomId, MemberModifiedType.JOIN, userId);

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
        saveUserOutput.save(user);

        modifiedMemberChatRoomEventOutput.publishModifiedMember(roomId, MemberModifiedType.LEAVE, userId);

        return chatRoom;
    }
}
