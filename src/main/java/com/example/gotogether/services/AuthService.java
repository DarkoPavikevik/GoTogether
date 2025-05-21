package com.example.gotogether.services;

import com.example.gotogether.dto.AuthRequest;
import com.example.gotogether.dto.AuthResponse;
import com.example.gotogether.dto.SignUpDTO;
import com.example.gotogether.dto.UserDTO;
import com.example.gotogether.enums.Role;
import com.example.gotogether.exceptions.ResourceNotFoundException;
import com.example.gotogether.model.User;
import com.example.gotogether.repositories.UserRepository;
import com.example.gotogether.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserDTO register(SignUpDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use.");
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phoneNumber(dto.getPhoneNumber())
                .role(Role.User)
                .build();

        User saved = userRepository.save(user);

        return UserDTO.builder()
                .id(saved.getId())
                .username(saved.getUsername())
                .email(saved.getEmail())
                .phoneNumber(saved.getPhoneNumber())
                .role(String.valueOf(saved.getRole()))
                .created(saved.getCreated())
                .build();
    }


    public AuthResponse login(AuthRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refreshToken(String refreshToken) {
        String username = jwtUtil.extractUsername(refreshToken); // renamed method

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String newAccessToken = jwtUtil.generateAccessToken(user);

        return new AuthResponse(newAccessToken, refreshToken);
    }
}
