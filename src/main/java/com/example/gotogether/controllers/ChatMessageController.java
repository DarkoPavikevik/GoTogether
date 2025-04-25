package com.example.gotogether.controllers;

import com.example.gotogether.dto.ChatMessageDTO;
import com.example.gotogether.services.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @GetMapping
    public List<ChatMessageDTO> getAll() {
        return chatMessageService.getAllMessages();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatMessageDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(chatMessageService.getMessageById(id));
    }

    @PostMapping
    public ResponseEntity<ChatMessageDTO> create(@RequestBody ChatMessageDTO dto) {
        return ResponseEntity.ok(chatMessageService.createMessage(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        chatMessageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}