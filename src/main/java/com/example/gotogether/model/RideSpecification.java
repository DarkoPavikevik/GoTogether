package com.example.gotogether.model;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class RideSpecification {

    public static Specification<Ride> hasFromLocation(String fromLocation) {
        return (root,query,builder) ->
                fromLocation == null ? null : builder.equal(root.get("fromLocation"), fromLocation);
    }

    public static Specification<Ride> hasToLocation(String toLocation) {
        return (root,query,builder) ->
                toLocation == null ? null : builder.equal(root.get("toLocation"), toLocation);
    }

    public static Specification<Ride> hasDate(LocalDate date) {
        return (root,query,builder) ->
                date == null ? null : builder.equal(root.get("date"), date);
    }
}
