package com.unimag.web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PassengerRepostory extends JpaRepository<PassengerRepostory, Long> {

Optional<PassengerRepostory> findByEmailIgnoreCase(String email);
    @Query("""
SELECT p FROM Passenger p
         LEFT JOIN FETCH p.profile
         WHERE LOWER(p.email) = LOWER(:email)
""")
    Optional<PassengerRepostory> fechtWhitProfileByEmail(String email);

}
