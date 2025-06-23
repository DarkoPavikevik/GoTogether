package com.example.gotogether.services;

import com.example.gotogether.dto.ChatMessageDTO;
import com.example.gotogether.exceptions.ResourceNotFoundException;
import com.example.gotogether.model.ChatMessage;
import com.example.gotogether.model.User;
import com.example.gotogether.repositories.ChatMessageRepository;
import com.example.gotogether.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    public ChatMessageDTO sendMessage(ChatMessageDTO dto) {
        User sender = userRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(dto.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        ChatMessage message = ChatMessage.builder()
                .sender(sender)
                .receiver(receiver)
                .message(dto.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        chatMessageRepository.save(message);

        dto.setId(message.getId());
        dto.setTimestamp(message.getTimestamp());

        return dto;
    }

    public List<ChatMessageDTO> getConversation(Long user1Id, Long user2Id) {
        List<ChatMessage> messages = chatMessageRepository
                .findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByTimestampAsc(
                        user1Id, user2Id, user1Id, user2Id
                );

        return messages.stream().map(msg -> ChatMessageDTO.builder()
                .id(msg.getId())
                .senderId(msg.getSender().getId())
                .receiverId(msg.getReceiver().getId())
                .message(msg.getMessage())
                .timestamp(msg.getTimestamp())
                .build()).toList();
    }
}

