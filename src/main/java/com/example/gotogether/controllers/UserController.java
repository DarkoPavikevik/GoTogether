package com.example.gotogether.controllers;

import com.example.gotogether.dto.UserDTO;
import com.example.gotogether.model.User;
import com.example.gotogether.repositories.UserRepository;
import com.example.gotogether.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
//@SecurityRequirement(name = "bearerAuth")

public class UserController {

    private final UserService userService;

    private final UserRepository userRepository;

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

    @PutMapping("preferences/{id}")
    public ResponseEntity<UserDTO> editUserPreferences(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO editUserPreference = userService.editPreferences(id,userDTO);
        return ResponseEntity.ok(editUserPreference);
    }

    @GetMapping("/{userId}/ride-dates")
    public ResponseEntity<List<LocalDate>> getUserRideDates(@PathVariable Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();
        List<LocalDate> rideDates = user.getAllRideDates();

        return ResponseEntity.ok(rideDates);
    }








}
