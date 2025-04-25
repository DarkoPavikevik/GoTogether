package com.example.gotogether.services;

import com.example.gotogether.dto.ChatMessageDTO;
import com.example.gotogether.exceptions.ResourceNotFoundException;
import com.example.gotogether.model.ChatMessage;
import com.example.gotogether.model.User;
import com.example.gotogether.repositories.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService; // Add this dependency

    public List<ChatMessageDTO> getAllMessages() {
        return chatMessageRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ChatMessageDTO getMessageById(Long id) {
        ChatMessage chat = chatMessageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id " + id));
        return mapToDTO(chat);
    }

    public ChatMessageDTO createMessage(ChatMessageDTO dto) {
        // Fetch actual user entities
        User sender = userService.findById(dto.getSenderId())
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
        User receiver = userService.findById(dto.getReceiverId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));

        ChatMessage msg = ChatMessage.builder()
                .sender(sender)
                .receiver(receiver)
                .message(dto.getMessage())
                .timestamp(dto.getTimestamp() != null ? dto.getTimestamp() : LocalDateTime.now())
                .build();

        ChatMessage saved = chatMessageRepository.save(msg);
        return mapToDTO(saved);
    }

    public void deleteMessage(Long id) {
        chatMessageRepository.deleteById(id);
    }

    public ChatMessageDTO mapToDTO(ChatMessage chat) {
        return ChatMessageDTO.builder()
                .id(chat.getId())
                .senderId(chat.getSender().getId())
                .receiverId(chat.getReceiver().getId())
                .message(chat.getMessage())
                .timestamp(chat.getTimestamp())
                .build();
    }
}
