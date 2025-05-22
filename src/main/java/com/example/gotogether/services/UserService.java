package com.example.gotogether.services;

import com.example.gotogether.dto.UserDTO;
import com.example.gotogether.dto.VehicleDTO;
import com.example.gotogether.enums.Role;
import com.example.gotogether.exceptions.ResourceNotFoundException;
import com.example.gotogether.model.User;
import com.example.gotogether.model.Vehicle;
import com.example.gotogether.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public UserDTO editUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (userDTO.getProfilePicture() != null) {
            user.setProfilePicture(userDTO.getProfilePicture());
        }
        if (userDTO.getName() != null) {
            user.setName(userDTO.getName());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getBio() != null) {
            user.setBio(userDTO.getBio());
        }

        User savedUser = userRepository.save(user);

        return mapToDto(savedUser);
    }



    public UserDTO mapToDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setProfilePicture(user.getProfilePicture());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setBio(user.getBio());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(String.valueOf(user.getRole()));
        dto.setRating(user.getRating());
        return dto;
    }


    public UserDTO editProfile(Long id, UserDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setBio(dto.getBio());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setProfilePicture(dto.getProfilePicture());
        User updatedVehicle = userRepository.save(user);

        return mapToDto(updatedVehicle);
    }


    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


}
