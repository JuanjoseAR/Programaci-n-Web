package com.unimag.web.domine.repositories;

import com.unimag.web.domain.Passenger;
import com.unimag.web.domain.PassengerProfile;
import com.unimag.web.repositories.PassengerRepository;
import com.unimag.web.repositories.PassengerProfileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PassengerRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private PassengerProfileRepository passengerProfileRepository;

    @Test
    @DisplayName("Passenger: buscar pasajero por email ignorando mayúsculas")
    void shouldFindByEmailIgnoreCase() {

        Passenger passenger = Passenger.builder()
                .fullName("Juan Pérez")
                .email("juan@example.com")
                .build();
        passengerRepository.save(passenger);


        Optional<Passenger> found = passengerRepository.findByEmailIgnoreCase("JUAN@EXAMPLE.COM");


        assertThat(found).isPresent();
        assertThat(found.get().getFullName()).isEqualTo("Juan Pérez");
    }

    @Test
    @DisplayName("Passenger: buscar pasajero con su perfil por email")
    void shouldFetchWithProfileByEmail() {

        PassengerProfile profile = PassengerProfile.builder()
                .phone("3001234567")
                .countryCode("COL")
                .build();
        passengerProfileRepository.save(profile);

        Passenger passenger = Passenger.builder()
                .fullName("María López")
                .email("maria@example.com")
                .profile(profile)
                .build();
        passengerRepository.save(passenger);


        Optional<Passenger> found = passengerRepository.fechtWhitProfileByEmail("maria@example.com");


        assertThat(found).isPresent();
        assertThat(found.get().getProfile()).isNotNull();
        assertThat(found.get().getProfile().getPhone()).isEqualTo("3001234567");
    }
}
