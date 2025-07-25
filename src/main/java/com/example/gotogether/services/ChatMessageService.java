package com.example.gotogether.services;

import com.example.gotogether.dto.ChatMessageDTO;
import com.example.gotogether.dto.ParticipantDTO;
import com.example.gotogether.dto.RideChatDTO;
import com.example.gotogether.dto.RideDTO;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    public RideChatDTO getChatForRide(Long rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));

        List<ChatMessage> messages = chatMessageRepository.findByRideIdOrderByTimestampAsc(rideId);

        // Extract participants
        Set<User> participants = new HashSet<>();
        for (ChatMessage msg : messages) {
            participants.add(msg.getSender());
            participants.add(msg.getReceiver());
        }

        // Map to DTO
        List<ParticipantDTO> participantDTOs = participants.stream().map(user ->
                ParticipantDTO.builder()
                        .id(user.getId())
                        .name(user.getUsername())
                        .avatar(user.getProfilePicture())
                        .isDriver(ride.getDriver().getId().equals(user.getId()))
                        .build()
        ).toList();

        // Map messages
        List<ChatMessageDTO> messageDTOs = messages.stream().map(msg ->
                ChatMessageDTO.builder()
                        .id(msg.getId())
                        .senderId(msg.getSender().getId())
                        .receiverId(msg.getReceiver().getId())
                        .message(msg.getMessage())
                        .timestamp(msg.getTimestamp())
                        .build()
        ).toList();

        RideDTO rideDTO = rideService.getRideById(rideId); // Assuming this method exists

        return RideChatDTO.builder()
                .ride(rideDTO)
                .participants(participantDTOs)
                .messages(messageDTOs)
                .build();
    }


}

