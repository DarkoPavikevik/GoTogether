package com.example.gotogether.repositories;

import com.example.gotogether.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email) ;
    boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);
}
