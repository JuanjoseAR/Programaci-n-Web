package com.unimag.web.api;

import com.unimag.web.api.dto.FlightDto.*;
import com.unimag.web.services.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
@Validated
public class FlightController {

    private final FlightService service;

    @PostMapping
    public ResponseEntity<FlightResponse> create(@Valid @RequestBody FlightCreateRequest req,
                                                 UriComponentsBuilder uriBuilder) {
        var body = service.create(req);
        var location = uriBuilder.path("/api/flights/{id}")
                .buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }


    @GetMapping
    public ResponseEntity<Page<FlightResponse>> list(
            @RequestParam(required = false) String number,
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) Long airlineId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "departureTime") String sort,
            @RequestParam(defaultValue = "desc") String dir) {

        var direction = "desc".equalsIgnoreCase(dir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        var pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        var result = service.search(number, origin, destination, airlineId, from, to, pageable);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/search/by-tags")
    public ResponseEntity<List<FlightResponse>> searchByTags(@RequestParam List<String> tags) {
        return ResponseEntity.ok(service.searchWithTags(tags));
    }

    @PostMapping("/{id}/tags/{tagId}")
    public ResponseEntity<FlightResponse> addTag(@PathVariable Long id, @PathVariable Long tagId) {
        return ResponseEntity.ok(service.addTag(id, tagId));
    }

    @DeleteMapping("/{id}/tags/{tagId}")
    public ResponseEntity<FlightResponse> removeTag(@PathVariable Long id, @PathVariable Long tagId) {
        return ResponseEntity.ok(service.removeTag(id, tagId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FlightResponse> update(@PathVariable Long id,
                                                 @Valid @RequestBody FlightUpdateRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
