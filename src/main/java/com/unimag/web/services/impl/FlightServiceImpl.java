package com.unimag.web.services.impl;

import com.unimag.web.api.dto.FlightDto;
import com.unimag.web.api.dto.FlightDto.FlightCreateRequest;
import com.unimag.web.api.dto.FlightDto.FlightResponse;
import com.unimag.web.domain.Airline;
import com.unimag.web.domain.Airport;
import com.unimag.web.domain.Flight;
import com.unimag.web.domain.Tag;
import com.unimag.web.exception.NotFoundException;
import com.unimag.web.repositories.AirlineRepository;
import com.unimag.web.repositories.AirportRepository;
import com.unimag.web.repositories.FlightRepository;
import com.unimag.web.repositories.TagRepository;
import com.unimag.web.services.FlightService;
import com.unimag.web.services.mapper.FlightMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class FlightServiceImpl implements FlightService {


    private final FlightRepository flightRepo;
    private final AirlineRepository airlineRepository;
    private final AirportRepository airportRepository;
    private final TagRepository tagRepository;
    private final FlightMapper flightMapper;

    @Override
    public FlightResponse create(FlightCreateRequest req) {
        Airline airline = airlineRepository.findById(req.airlineId())
                .orElseThrow(() -> new NotFoundException("Airline %d not found".formatted(req.airlineId())));
        Airport origin = airportRepository.findById(req.originAirportId())
                .orElseThrow(() -> new NotFoundException("Airport %d not found".formatted(req.originAirportId())));
        Airport destination = airportRepository.findById(req.destinationAirportId())
                .orElseThrow(() -> new NotFoundException("Airport %d not found".formatted(req.destinationAirportId())));

        Flight flight = flightMapper.toEntity(req);
        flight.setAirline(airline);
        flight.setOrigin(origin);
        flight.setDestination(destination);

        if (req.tagIds() != null && !req.tagIds().isEmpty()) {
            var tags = tagRepository.findAllById(req.tagIds());
            if (tags.size() != req.tagIds().size()) {
                throw new NotFoundException("Some tags not found for IDs: " + req.tagIds());
            }
            flight.setTags(Set.copyOf(tags));
        }

        return flightMapper.toResponse(flightRepo.save(flight));
    }

    @Override
    @Transactional(readOnly = true)
    public FlightResponse get(Long id) {
        return flightRepo.findById(id)
                .map(flightMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Flight %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FlightResponse> listByAirline(String airlineName, Pageable pageable) {
        return flightRepo.findByAirlineName(airlineName, pageable)
                .map(flightMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FlightResponse> search(
            String number,
            String origin,
            String destination,
            Long airlineId,
            OffsetDateTime from,
            OffsetDateTime to,
            Pageable pageable) {

        return flightRepo.search(number, origin, destination, airlineId, from, to, pageable)
                .map(flightMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlightResponse> searchWithTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) return List.of();
        return flightRepo.findByAllTags(tags, tags.size()).stream()
                .map(flightMapper::toResponse)
                .toList();
    }

    @Override
    public FlightResponse addTag(Long flightId, Long tagId) {
        Flight flight = flightRepo.findById(flightId)
                .orElseThrow(() -> new NotFoundException("Flight %d not found".formatted(flightId)));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new NotFoundException("Tag %d not found".formatted(tagId)));

        flight.addTag(tag);
        return flightMapper.toResponse(flight);
    }

    @Override
    public FlightResponse removeTag(Long flightId, Long tagId) {
        Flight flight = flightRepo.findById(flightId)
                .orElseThrow(() -> new NotFoundException("Flight %d not found".formatted(flightId)));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new NotFoundException("Tag %d not found".formatted(tagId)));

        flight.getTags().remove(tag);
        tag.getFlights().remove(flight);
        return flightMapper.toResponse(flight);
    }

    @Override
    public void delete(Long id) {
        if (!flightRepo.existsById(id)) {
            throw new NotFoundException("Flight %d not found".formatted(id));
        }
        flightRepo.deleteById(id);
    }

    @Override
    public FlightDto.FlightResponse update(Long id, FlightDto.FlightUpdateRequest req) {
        var flight = flightRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Flight con id " + id + " no encontrado."));

        if (req.number() != null)        flight.setNumber(req.number());
        if (req.departureTime() != null) flight.setDepartureTime(req.departureTime());
        if (req.arrivalTime() != null)   flight.setArrivalTime(req.arrivalTime());

        if (req.airlineId() != null) {
            var airline = airlineRepository.findById(req.airlineId())
                    .orElseThrow(() -> new NotFoundException("Airline con id " + req.airlineId() + " no encontrada."));
            flight.setAirline(airline);
        }

        if (req.originAirportId() != null) {
            var origin = airportRepository.findById(req.originAirportId())
                    .orElseThrow(() -> new NotFoundException("Airport origen con id " + req.originAirportId() + " no encontrado."));
            flight.setOrigin(origin);
        }

        if (req.destinationAirportId() != null) {
            var dest = airportRepository.findById(req.destinationAirportId())
                    .orElseThrow(() -> new NotFoundException("Airport destino con id " + req.destinationAirportId() + " no encontrado."));
            flight.setDestination(dest);
        }

        if (req.tagIds() != null) {
            var tags = tagRepository.findAllById(req.tagIds());
            flight.setTags(new HashSet<>(tags));
        }

        var saved = flightRepo.save(flight);
        return flightMapper.toResponse(saved);
    }
}
