package com.example.gotogether.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private User reviewer;

    @ManyToOne
    @JsonIgnore
    private User reviewedUser;

    @ManyToOne
    @JsonIgnore
    private Ride ride;

    private int rating;
    private String comment;


    public Review(Long id) {
        this.id = id;
    }
}
