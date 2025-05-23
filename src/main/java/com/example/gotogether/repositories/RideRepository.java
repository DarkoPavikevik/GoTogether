package com.example.gotogether.repositories;

import com.example.gotogether.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long>, JpaSpecificationExecutor<Ride> {

    List<Ride> findByFromLocationAndToLocation(String from, String to);
    List<Ride> findByDriverId(Long driverId);
}