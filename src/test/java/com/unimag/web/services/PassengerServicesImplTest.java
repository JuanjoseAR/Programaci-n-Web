package com.unimag.web.services;

import com.unimag.web.api.dto.PassengerDto.*;
import com.unimag.web.domain.Passenger;
import com.unimag.web.domain.PassengerProfile;
import com.unimag.web.repositories.PassengerProfileRepository;
import com.unimag.web.repositories.PassengerRepository;
import com.unimag.web.services.impl.PassengerServicesImpl;
import com.unimag.web.services.mapper.PassengerMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassengerServicesImplTest {

    @Mock PassengerRepository passengerRepository;
    @Mock PassengerProfileRepository profileRepository;
    @Mock PassengerMapper passengerMapper;

    @InjectMocks
    PassengerServicesImpl service;
    @Test
    void shouldCreatePassengerWithProfile() {
        var profileDto = new PassengerProfileDto("3001234567", "+57");
        var req = new PassengerCreateRequest("Juan", "juan@test.com", profileDto);

        var profileFromMapper = PassengerProfile.builder()
                .phone("3001234567")
                .countryCode("+57")
                .build();

        var profileSaved = PassengerProfile.builder()
                .id(1L)
                .phone("3001234567")
                .countryCode("+57")
                .build();

        var passengerFromMapper = Passenger.builder()
                .id(10L)
                .fullName("Juan")
                .email("juan@test.com")
                .build();

        var passengerSaved = Passenger.builder()
                .id(10L)
                .fullName("Juan")
                .email("juan@test.com")
                .profile(profileSaved)
                .build();

        var response = new PassengerResponse(10L, "Juan", "juan@test.com", profileDto);

        when(passengerMapper.toEntity(profileDto)).thenReturn(profileFromMapper);

        when(passengerMapper.toEntity(req)).thenReturn(passengerFromMapper);
        when(profileRepository.save(any(PassengerProfile.class))).thenReturn(profileSaved);
        when(passengerRepository.save(any(Passenger.class))).thenReturn(passengerSaved);
        when(passengerMapper.toResponse(passengerSaved)).thenReturn(response);

        var res = service.createPassenger(req);

        assertThat(res.id()).isEqualTo(10L);
        assertThat(res.email()).isEqualTo("juan@test.com");

        verify(passengerMapper).toEntity(profileDto);
        verify(profileRepository).save(any(PassengerProfile.class));
        verify(passengerRepository).save(any(Passenger.class));
        verify(passengerMapper).toEntity(req);
        verify(passengerMapper).toResponse(passengerSaved);
    }



    @Test
    void shouldUpdateExistingPassenger() {
        var profile = PassengerProfile.builder()
                .phone("111").countryCode("+11")
                .build();

        var passenger = Passenger.builder()
                .id(5L).fullName("Old")
                .email("old@test.com")
                .profile(profile)
                .build();

        var req = new PassengerUpdateRequest(
                "New", "old@test.com",
                new PassengerProfileDto("222", "+22")
        );

        var updatedResponse = new PassengerResponse(
                5L, "New", "old@test.com",
                new PassengerProfileDto("222", "+22")
        );

        when(passengerRepository.findByEmailIgnoreCase("old@test.com"))
                .thenReturn(Optional.of(passenger));
        when(profileRepository.save(any())).thenReturn(profile);
        when(passengerRepository.save(any())).thenReturn(passenger);

        try (MockedStatic<PassengerMapper> mocked = mockStatic(PassengerMapper.class)) {
            mocked.when(() -> passengerMapper.toResponse(any()))
                    .thenReturn(updatedResponse);

            var res = service.updatePassenger(req);

            assertThat(res.fullname()).isEqualTo("New");
            assertThat(res.profile().phone()).isEqualTo("222");
        }
    }


    @Test
    void shouldThrowWhenPassengerNotFoundOnUpdate() {
        var req = new PassengerUpdateRequest( "X","missing@test.com", null);

        when(passengerRepository.findByEmailIgnoreCase("missing@test.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updatePassenger(req))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Passenger not found");
    }
    @Test
    void shouldGetPassengerByEmail() {
        var passenger = Passenger.builder()
                .id(7L)
                .fullName("Ana")
                .email("ana@test.com")
                .build();

        var response = new PassengerResponse(
                7L,
                "Ana",
                "ana@test.com",
                null
        );

        when(passengerRepository.fechtWhitProfileByEmail("ana@test.com"))
                .thenReturn(Optional.of(passenger));

        try (MockedStatic<PassengerMapper> mocked = mockStatic(PassengerMapper.class)) {
            mocked.when(() -> passengerMapper.toResponse(passenger))
                    .thenReturn(response);

            var res = service.getByEmail("ana@test.com");

            assertThat(res.fullname()).isEqualTo("Ana");
            verify(passengerRepository).fechtWhitProfileByEmail("ana@test.com");
        }
    }

    @Test
    void shouldThrowWhenGetByEmailNotFound() {
        when(passengerRepository.fechtWhitProfileByEmail("none@test.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getByEmail("none@test.com"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldGetPassengerById() {
        var passenger = Passenger.builder()
                .id(3L)
                .fullName("Carlos")
                .email("c@test.com")
                .build();

        var response = new PassengerResponse(
                3L,
                "Carlos",
                "c@test.com",
                null
        );

        when(passengerRepository.findById(3L))
                .thenReturn(Optional.of(passenger));

        try (MockedStatic<PassengerMapper> mocked = mockStatic(PassengerMapper.class)) {
            mocked.when(() -> passengerMapper.toResponse(passenger))
                    .thenReturn(response);

            var res = service.getById(3L);

            assertThat(res.id()).isEqualTo(3L);
            assertThat(res.fullname()).isEqualTo("Carlos");
        }
    }


    @Test
    void shouldDeletePassenger() {
        var passenger = Passenger.builder().id(9L).fullName("Luis").email("l@test.com").build();
        when(passengerRepository.findById(9L)).thenReturn(Optional.of(passenger));

        service.deletePassenger(9L);

        verify(passengerRepository).delete(passenger);
    }

    @Test
    void shouldThrowWhenDeleteNotFound() {
        when(passengerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deletePassenger(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldListPassengersPaged() {
        var passenger = Passenger.builder()
                .id(1L)
                .fullName("A")
                .email("a@test.com")
                .build();

        var response = new PassengerResponse(
                1L,
                "A",
                "a@test.com",
                null
        );

        var page = new PageImpl<>(List.of(passenger));

        when(passengerRepository.findAll(PageRequest.of(0, 5)))
                .thenReturn(page);

        try (MockedStatic<PassengerMapper> mocked = mockStatic(PassengerMapper.class)) {
            mocked.when(() -> passengerMapper.toResponse(passenger))
                    .thenReturn(response);

            var result = service.list(PageRequest.of(0, 5));

            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getContent().get(0).email()).isEqualTo("a@test.com");
        }
    }

}