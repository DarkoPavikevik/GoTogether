package com.example.gotogether.model;

import com.example.gotogether.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Table(name = "users")
@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true,nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // hashed password

    @Enumerated(EnumType.STRING)
    private Role role; // Enum: PASSENGER, DRIVER, ADMIN

    private Double rating = 0.0;

    private String location;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Vehicle> vehicles = new HashSet<>();

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
    private Set<Ride> rides = new HashSet<>();


    public User(Long id) {
        this.id = id;
    }


}
