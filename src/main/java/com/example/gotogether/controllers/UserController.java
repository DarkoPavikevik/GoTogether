package com.example.gotogether.controllers;

import com.example.gotogether.dto.UserDTO;
import com.example.gotogether.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
//@SecurityRequirement(name = "bearerAuth")

public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> editUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO editerUser = userService.editUser(id,userDTO);
        return ResponseEntity.ok(editerUser);
    }







}
