package com.unimag.web.api;

import com.unimag.web.api.dto.FlightDto.*;
import com.unimag.web.services.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class FlightController {

    private final FlightService service;


    @PostMapping("/flights")
    public ResponseEntity<FlightResponse> create(@Valid @RequestBody FlightCreateRequest req,
                                                 UriComponentsBuilder uriBuilder) {
        var body = service.create(req);
        var location = uriBuilder.path("/api/flights/{id}")
                .buildAndExpand(body.id())
                .toUri();
        return ResponseEntity.created(location).body(body);
    }


    @GetMapping("/flights/{id}")
    public ResponseEntity<FlightResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping("/flights/by-airline")
    public ResponseEntity<?> listByAirline(@RequestParam String airlineName,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.listByAirline(airlineName, pageable));
    }


    @GetMapping("/flights/search")
    public ResponseEntity<?> search(@RequestParam String origin,
                                    @RequestParam String destination,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime from,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime to,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.search(origin, destination, from, to, pageable));
    }


    @GetMapping("/flights/by-tags")
    public ResponseEntity<List<FlightResponse>> searchWithTags(@RequestParam List<String> tags) {
        return ResponseEntity.ok(service.searchWithTags(tags));
    }


    @PostMapping("/flights/{flightId}/tags/{tagId}")
    public ResponseEntity<FlightResponse> addTag(@PathVariable Long flightId,
                                                 @PathVariable Long tagId) {
        return ResponseEntity.ok(service.addTag(flightId, tagId));
    }


    @DeleteMapping("/flights/{flightId}/tags/{tagId}")
    public ResponseEntity<FlightResponse> removeTag(@PathVariable Long flightId,
                                                    @PathVariable Long tagId) {
        return ResponseEntity.ok(service.removeTag(flightId, tagId));
    }


    @DeleteMapping("/flights/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
