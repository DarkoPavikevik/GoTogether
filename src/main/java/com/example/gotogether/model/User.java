package com.example.gotogether.model;

import com.example.gotogether.enums.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Table(name = "users")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String bio;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true,nullable = false)
    private String username;

    @Column(length = 20, unique = true)
    private String phoneNumber;


    @Column(nullable = false)
    private String password; // hashed password

    @Enumerated(EnumType.STRING)
    private Role role;

    private Double rating = 0.0;

    private String location;

    private String profilePicture;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate created;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Vehicle> vehicles = new HashSet<>();

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Ride> rides = new HashSet<>();



    public User(Long id) {
        this.id = id;
    }

    @PrePersist
    protected void onCreate() {
        this.created = LocalDate.now();
    }


}
