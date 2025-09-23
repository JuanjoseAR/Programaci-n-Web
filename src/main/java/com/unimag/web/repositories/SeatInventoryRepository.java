package com.unimag.web.repositories;

import com.unimag.web.domain.Cabin;
import com.unimag.web.domain.SeatInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SeatInventoryRepository extends JpaRepository<SeatInventory, Integer> {

    Optional<SeatInventory> findByFlightIdAndCabin(Long flightId, Cabin cabin);

    @Query("""
        SELECT CASE WHEN si.availableSeats >= :min THEN true ELSE false END
        FROM SeatInventory si
        WHERE si.flight.id = :flightId AND si.cabin = :cabin
    """)
    boolean hasAvailableSeats(@Param("flightId") Long flightId,
                              @Param("cabin") Cabin cabin,
                              @Param("min") int min);

}
