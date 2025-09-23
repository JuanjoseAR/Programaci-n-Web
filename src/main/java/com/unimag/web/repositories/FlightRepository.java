package com.unimag.web.repositories;

import com.unimag.web.domain.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;


public interface FlightRepository extends JpaRepository<Flight, Long> {


  Page<Flight> findByAirlineName(String airlineName, Pageable pageable);

  Page<Flight> findByOriginCodeAndDestinationCodeAndDepartureTimeBetween(
          String originCode,
          String destinationCode,
          OffsetDateTime from,
          OffsetDateTime to,
          Pageable pageable
  );


    @Query("""
        SELECT DISTINCT f FROM Flight f
        JOIN FETCH f.airline
        JOIN FETCH f.origin
        JOIN FETCH f.destination
        LEFT JOIN FETCH f.tags
        WHERE (:origin IS NULL OR f.origin.code = :origin)
          AND (:destination IS NULL OR f.destination.code = :destination)
          AND f.departureTime BETWEEN :from AND :to
    """)
    List<Flight> searchFlights(
            @Param("origin") String origin,
            @Param("destination") String destination,
            @Param("from") OffsetDateTime from,
            @Param("to") OffsetDateTime to
    );

    @Query(value = """
        SELECT f.* FROM flight f
        JOIN flight_tags ft ON f.id = ft.flight_id
        JOIN tag t ON t.id = ft.tag_id
        WHERE t.name IN (:tags)
        GROUP BY f.id
        HAVING COUNT(DISTINCT t.id) = :required
    """, nativeQuery = true)
    List<Flight> findByAllTags(
            @Param("tags") Collection<String> tags,
            @Param("required") long required
    );
}
