package com.example.gotogether.services;


import com.example.gotogether.model.Ride;
import com.example.gotogether.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${MAIL_USER}")
    private String supportEmail;

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

    public void sendSupportMessage(String fullName, String email, String category, String subject, String messageContent) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(supportEmail);
        message.setSubject("[Support] " + subject);
        message.setText(
                "New support message from: " + fullName + " (" + email + ")\n\n" +
                        "Category: " + category + "\n\n" +
                        "Message:\n" + messageContent
        );

        mailSender.send(message);
    }

    public void sendLowRatingAlert(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(supportEmail);
        message.setSubject("Low-Rated User Alert");

        message.setText(
                "User " + user.getUsername() + " (email: " + user.getEmail() + ") " +
                        "has a very low rating of " + user.getRating() + ".\n\n" +
                        "This user may be harming the platform's experience. Please consider reviewing their activity/profile."
        );

        mailSender.send(message);
    }



}
