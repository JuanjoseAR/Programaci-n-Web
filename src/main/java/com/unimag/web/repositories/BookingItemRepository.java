package com.unimag.web.repositories;

import com.unimag.web.domain.BookingItem;
import com.unimag.web.domain.Cabin;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BookingItemRepository extends JpaRepository<BookingItem, Long> {
    @EntityGraph(attributePaths = {"flight"})
    List<BookingItem> findByBookingIdOrderBySegmentOrderAsc(Long bookingId);

    @Query("""
           SELECT COALESCE(SUM(bi.price), 0)
           FROM BookingItem bi
           WHERE bi.booking.id = :bookingId
           """)
    BigDecimal calculateTotalByBookingId(@Param("bookingId") Long bookingId);

    @Query("""
           SELECT COUNT(bi)
           FROM BookingItem bi
           WHERE bi.flight.id = :flightId
             AND bi.cabin = :cabin
           """)
    long countReservedSeatsByFlightAndCabin(@Param("flightId") Long flightId,
                                            @Param("cabin") Cabin cabin);


}
