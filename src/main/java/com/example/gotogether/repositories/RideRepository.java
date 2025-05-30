package com.example.gotogether.repositories;

import com.example.gotogether.dto.PopularRouteDTO;
import com.example.gotogether.model.Ride;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long>, JpaSpecificationExecutor<Ride> {


    @Query("SELECT new com.example.gotogether.dto.PopularRouteDTO(r.fromLocation, r.toLocation, COUNT(r)) " +
            "FROM Ride r " +
            "GROUP BY r.fromLocation, r.toLocation " +
            "ORDER BY COUNT(r) DESC")
    List<PopularRouteDTO> findTopPopularRoutes(Pageable pageable);
    List<Ride> findByFromLocationAndToLocation(String from, String to);
    List<Ride> findByDriverId(Long driverId);
}