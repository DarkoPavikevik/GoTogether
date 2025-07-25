package com.example.gotogether.repositories;

import com.example.gotogether.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderIdAndReceiverIdAndRideIdOrReceiverIdAndSenderIdAndRideIdOrderByTimestampAsc(
            Long senderId1, Long receiverId1, Long rideId1,
            Long senderId2, Long receiverId2, Long rideId2
    );

    List<ChatMessage> findByRideIdOrderByTimestampAsc(Long rideId);

}