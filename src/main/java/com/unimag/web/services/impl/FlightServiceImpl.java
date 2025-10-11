package com.unimag.web.services.impl;

import com.unimag.web.api.dto.FlightDto.*;
import com.unimag.web.domain.Airline;
import com.unimag.web.domain.Airport;
import com.unimag.web.domain.Flight;
import com.unimag.web.domain.Tag;
import com.unimag.web.exception.NotFoundException;
import com.unimag.web.repositories.*;
import com.unimag.web.services.FlightService;
import com.unimag.web.services.mapper.FlightMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepo;
    private final AirlineRepository airlineRepo;
    private final AirportRepository airportRepo;
    private final TagRepository tagRepo;

    @Override
    public FlightResponse create(FlightCreateRequest req) {
        Airline airline = airlineRepo.findById(req.airlineId())
                .orElseThrow(() -> new NotFoundException("Airline %d not found".formatted(req.airlineId())));
        Airport origin = airportRepo.findById(req.originAirportId())
                .orElseThrow(() -> new NotFoundException("Airport %d not found".formatted(req.originAirportId())));
        Airport destination = airportRepo.findById(req.destinationAirportId())
                .orElseThrow(() -> new NotFoundException("Airport %d not found".formatted(req.destinationAirportId())));

        Flight flight = FlightMapper.toEntity(req);
        flight.setAirline(airline);
        flight.setOrigin(origin);
        flight.setDestination(destination);


        if (req.tagIds() != null && !req.tagIds().isEmpty()) {
            var tags = tagRepo.findAllById(req.tagIds());
            if (tags.size() != req.tagIds().size()) {
                throw new NotFoundException("Some tags not found for IDs: " + req.tagIds());
            }
            flight.setTags(Set.copyOf(tags));
        }

        return FlightMapper.toResponse(flightRepo.save(flight));
    }

    @Override
    @Transactional(readOnly = true)
    public FlightResponse get(Long id) {
        return flightRepo.findById(id)
                .map(FlightMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Flight %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FlightResponse> listByAirline(String airlineName, Pageable pageable) {
        return flightRepo.findByAirlineName(airlineName, pageable)
                .map(FlightMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FlightResponse> search(String origin, String destination, OffsetDateTime from, OffsetDateTime to, Pageable pageable) {
        return flightRepo.findByOriginCodeAndDestinationCodeAndDepartureTimeBetween(origin, destination, from, to, pageable)
                .map(FlightMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlightResponse> searchWithTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) return List.of();
        return flightRepo.findByAllTags(tags, tags.size()).stream()
                .map(FlightMapper::toResponse)
                .toList();
    }

    @Override
    public FlightResponse addTag(Long flightId, Long tagId) {
        Flight flight = flightRepo.findById(flightId)
                .orElseThrow(() -> new NotFoundException("Flight %d not found".formatted(flightId)));
        Tag tag = tagRepo.findById(tagId)
                .orElseThrow(() -> new NotFoundException("Tag %d not found".formatted(tagId)));

        flight.addTag(tag);
        return FlightMapper.toResponse(flight);
    }

    @Override
    public FlightResponse removeTag(Long flightId, Long tagId) {
        Flight flight = flightRepo.findById(flightId)
                .orElseThrow(() -> new NotFoundException("Flight %d not found".formatted(flightId)));
        Tag tag = tagRepo.findById(tagId)
                .orElseThrow(() -> new NotFoundException("Tag %d not found".formatted(tagId)));

        flight.getTags().remove(tag);
        tag.getFlights().remove(flight);

        return FlightMapper.toResponse(flight);
    }

    @Override
    public void delete(Long id) {
        if (!flightRepo.existsById(id)) {
            throw new NotFoundException("Flight %d not found".formatted(id));
        }
        flightRepo.deleteById(id);
    }
}
