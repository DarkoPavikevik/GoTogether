package com.example.gotogether.services;

import com.example.gotogether.dto.ReviewDTO;
import com.example.gotogether.dto.UserDTO;
import com.example.gotogether.dto.VehicleDTO;
import com.example.gotogether.enums.Role;
import com.example.gotogether.exceptions.ResourceNotFoundException;
import com.example.gotogether.model.Review;
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
            user.setName(userDTO.getUsername());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getBio() != null) {
            user.setBio(userDTO.getBio());
        }
        if (userDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(userDTO.getPhoneNumber());
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
        dto.setSmoking(user.getSmoking());
        dto.setPets(user.getPets());
        dto.setMusic(user.getMusic());
        dto.setNumberOfRides(user.getNumberOfRides());
        dto.setTalking(user.getTalking());
        dto.setVehicle(mapToDto(user.getVehicle()));
        dto.setReviews(
                user.getReviews()
                        .stream()
                        .map(this::mapToReviewDto)
                        .collect(Collectors.toList())
        );

        return dto;
    }


    public UserDTO editPreferences(Long id, UserDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        user.setSmoking(dto.getSmoking());
        user.setPets(dto.getPets());
        user.setTalking(dto.getTalking());
        user.setMusic(dto.getMusic());
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

    private VehicleDTO mapToDto(Vehicle vehicle) {
        if(vehicle == null) {return null;}
        return VehicleDTO.builder()
                .id(vehicle.getId())
                .userId(vehicle.getUser().getId())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .picture(vehicle.getPicture())
                .plateNumber(vehicle.getPlateNumber())
                .seats(vehicle.getSeats())
                .year(vehicle.getYear())
                .color(vehicle.getColor())
                .airCondition(vehicle.getAirCondition())
                .usbCharging(vehicle.getUsbCharging())
                .music(vehicle.getMusic())
                .comfortableSeats(vehicle.getComfortableSeats())
                .build();
    }

    private ReviewDTO mapToReviewDto(Review review) {
        if(review == null)
        {
            return null;
        }
        return ReviewDTO.builder()
                .id(review.getId())
                .reviewerPicture(review.getReviewer().getProfilePicture())
                .reviewerName(review.getReviewer().getUsername())
                .reviewerId(review.getReviewer().getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .commentDate(review.getCommentDate())
                .comment(review.getComment())
                .reviewedUserId(review.getReviewedUser().getId())
                .reviewerPicture(review.getReviewer().getProfilePicture())
                .build();
    }


}
