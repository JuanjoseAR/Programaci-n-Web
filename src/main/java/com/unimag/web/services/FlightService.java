package com.unimag.web.services;

import com.unimag.web.api.dto.FlightDto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;

public interface FlightService {
    FlightResponse create(FlightCreateRequest req);
    FlightResponse get(Long id);
    Page<FlightResponse> listByAirline(String airlineName, Pageable pageable);
    Page<FlightResponse> search(String number, String origin, String destination, Long airlineId, OffsetDateTime from, OffsetDateTime to, Pageable pageable);    List<FlightResponse> searchWithTags(List<String> tags);
    FlightResponse addTag(Long flightId, Long tagId);
    FlightResponse removeTag(Long flightId, Long tagId);
    FlightResponse update(Long id, FlightUpdateRequest req);
    void delete(Long id);
}
