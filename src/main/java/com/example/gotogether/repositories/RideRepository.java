package com.example.gotogether.repositories;

import com.example.gotogether.dto.PopularRouteDTO;
import com.example.gotogether.dto.RideDTO;
import com.example.gotogether.model.Ride;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
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

    @Query("SELECT r FROM Ride r WHERE r.driver.id = :userId AND (r.date < :today OR (r.date = :today AND r.time < :nowTime))")
    List<Ride> findPastRidesByUser(@Param("userId") Long userId,
                                   @Param("today") LocalDate today,
                                   @Param("nowTime") LocalTime nowTime);

    @Query("SELECT r FROM Ride r WHERE r.driver.id = :userId AND (r.date > :today OR (r.date = :today AND r.time >= :nowTime))")
    List<Ride> findFutureRidesByUser(@Param("userId") Long userId,
                                     @Param("today") LocalDate today,
                                     @Param("nowTime") LocalTime nowTime);
}