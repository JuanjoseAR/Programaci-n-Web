package com.unimag.web.domine.repositories;

import com.unimag.web.domain.Airport;
import com.unimag.web.repositories.AirportRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AirportRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private AirportRepository airportRepository;

    @Test
    @DisplayName("Airport: guardar y recuperar un aeropuerto por código")
    void shouldSaveAndFindByCode() {

        Airport airport = Airport.builder()
                .code("SMR")
                .city("Santa Marta")
                .name("Simón Bolívar International Airport")
                .build();

        airportRepository.save(airport);


        Optional<Airport> found = airportRepository.findByCode("SMR");


        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Simón Bolívar International Airport");
        assertThat(found.get().getCity()).isEqualTo("Santa Marta");


    }
}
