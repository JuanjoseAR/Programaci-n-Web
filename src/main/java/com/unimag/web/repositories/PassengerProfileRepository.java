package com.unimag.web.repositories;

import com.unimag.web.domain.PassengerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerProfileRepository extends JpaRepository<PassengerProfile, Long> {
}
