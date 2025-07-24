package com.example.gotogether.services;

import com.example.gotogether.dto.ChatMessageDTO;
import com.example.gotogether.exceptions.ResourceNotFoundException;
import com.example.gotogether.model.ChatMessage;
import com.example.gotogether.model.Ride;
import com.example.gotogether.model.User;
import com.example.gotogether.repositories.ChatMessageRepository;
import com.example.gotogether.repositories.RideRepository;
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

    private final RideRepository rideRepository;

    private final RideService rideService;

    public ChatMessageDTO sendMessage(ChatMessageDTO dto) {
        User sender = userRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(dto.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Ride ride = null;
        if (dto.getRideId() != null) {
            ride = rideRepository.findById(dto.getRideId())
                    .orElseThrow(() -> new RuntimeException("Ride not found"));
        }

        ChatMessage message = ChatMessage.builder()
                .sender(sender)
                .receiver(receiver)
                .ride(ride)
                .message(dto.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        ChatMessage saved = chatMessageRepository.save(message);

        return ChatMessageDTO.builder()
                .id(saved.getId())
                .senderId(sender.getId())
                .receiverId(receiver.getId())
                .rideId(ride != null ? ride.getId() : null)
                .ride(ride != null ? rideService.mapToDTO(ride) : null) // Optional: Include full ride info
                .message(saved.getMessage())
                .timestamp(saved.getTimestamp())
                .build();
    }


    public List<ChatMessageDTO> getConversation(Long user1Id, Long user2Id, Long rideId) {
        List<ChatMessage> messages = chatMessageRepository
                .findBySenderIdAndReceiverIdAndRideIdOrReceiverIdAndSenderIdAndRideIdOrderByTimestampAsc(
                        user1Id, user2Id, rideId,
                        user1Id, user2Id, rideId
                );

        return messages.stream()
                .map(msg -> ChatMessageDTO.builder()
                        .id(msg.getId())
                        .senderId(msg.getSender().getId())
                        .receiverId(msg.getReceiver().getId())
                        .rideId(msg.getRide() != null ? msg.getRide().getId() : null)
                        .message(msg.getMessage())
                        .timestamp(msg.getTimestamp())
                        .build())
                .collect(Collectors.toList());
    }


}

