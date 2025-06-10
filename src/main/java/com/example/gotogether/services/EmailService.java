package com.example.gotogether.services;


import com.example.gotogether.model.Ride;
import com.example.gotogether.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendBookingRequestEmail(User rideOwner, User requester, Ride ride) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(rideOwner.getEmail());
        message.setSubject("New Ride Booking Request");
        message.setText("Hello " + rideOwner.getUsername() + ",\n\n" +
                "User " + requester.getUsername() + " has requested to join your ride from "
                + ride.getFromLocation() + " to " + ride.getToLocation() + " on " + ride.getDate() + ".\n\n" +
                "Please check your dashboard to confirm or reject the request.");

        mailSender.send(message);
    }
}
