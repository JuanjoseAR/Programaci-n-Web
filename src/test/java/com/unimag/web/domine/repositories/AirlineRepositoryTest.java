package com.unimag.web.domine.repositories;

import com.unimag.web.domain.Airline;
import com.unimag.web.repositories.AirlineRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
class AirlineRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private AirlineRepository airlineRepository;

    @Test
    @DisplayName("Airline: Obtener una aerolinea por codigo")
    void shouldSaveAndFindByCode() {

        Airline airline =  Airline.builder().code("AV").name("Avianca").build();
        airlineRepository.save(airline);

        Optional<Airline> found = airlineRepository.findByCode("AV");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Avianca");
    }

}