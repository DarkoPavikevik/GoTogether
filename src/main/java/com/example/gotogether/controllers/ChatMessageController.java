package com.example.gotogether.controllers;

import com.example.gotogether.dto.ChatMessageDTO;
import com.example.gotogether.services.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @PostMapping("/send")
    public ResponseEntity<ChatMessageDTO> sendMessage(@RequestBody ChatMessageDTO dto) {
        return ResponseEntity.ok(chatMessageService.sendMessage(dto));
    }

    @GetMapping("/conversation")
    public ResponseEntity<List<ChatMessageDTO>> getConversation(
            @RequestParam Long user1Id,
            @RequestParam Long user2Id
    ) {
        return ResponseEntity.ok(chatMessageService.getConversation(user1Id, user2Id));
    }
}