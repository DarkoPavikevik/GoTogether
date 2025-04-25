package com.example.gotogether.services;

import com.example.gotogether.dto.NotificationDTO;
import com.example.gotogether.exceptions.ResourceNotFoundException;
import com.example.gotogether.model.Notification;
import com.example.gotogether.model.User;
import com.example.gotogether.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    @Autowired
    private final NotificationRepository notificationRepository;

    public List<NotificationDTO> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public NotificationDTO getNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id " + id));
        return mapToDTO(notification);
    }

    public NotificationDTO createNotification(NotificationDTO dto) {
        Notification notification = Notification.builder()
                .user(new User(dto.getUserId()))
                .message(dto.getMessage())
                .isRead(false)
                .sentAt(dto.getSentAt() != null ? dto.getSentAt() : LocalDateTime.now())
                .build();

        Notification saved = notificationRepository.save(notification);
        return mapToDTO(saved);
    }

    public void deleteNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Notification not found with id " + id);
        }
        notificationRepository.deleteById(id);
    }

    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id " + id));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    // Custom: Notify when request accepted
    public void sendRequestAcceptedNotification(Long userId, String driverUsername) {
        Notification notification = Notification.builder()
                .user(new User(userId))
                .message("Your ride request was accepted by " + driverUsername + ".")
                .isRead(false)
                .sentAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }

    public NotificationDTO mapToDTO(Notification n) {
        return NotificationDTO.builder()
                .id(n.getId())
                .userId(n.getUser().getId())
                .message(n.getMessage())
                .isRead(n.isRead())
                .sentAt(n.getSentAt())
                .build();
    }
}
