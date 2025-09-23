package com.unimag.web.repositories;

import com.unimag.web.domain.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByPassengerEmailIgnoreCaseOrderByCreatedAtDesc(String email, Pageable pageable);

    @Query("""
        SELECT b
        FROM Booking b
        LEFT JOIN FETCH b.items i
        LEFT JOIN FETCH i.flight f
        LEFT JOIN FETCH b.passenger p
        WHERE b.id = :id
    """)
    Optional<Booking> findByIdWithDetails(@Param("id") Long id);
}
