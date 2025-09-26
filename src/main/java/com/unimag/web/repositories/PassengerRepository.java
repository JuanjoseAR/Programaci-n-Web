package com.unimag.web.repositories;

import com.unimag.web.domain.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {

Optional<Passenger> findByEmailIgnoreCase(String email);
    @Query("""
SELECT p FROM Passenger p
         LEFT JOIN FETCH p.profile
         WHERE LOWER(p.email) = LOWER(:email)
""")
    Optional<Passenger> fechtWhitProfileByEmail(String email);

}
