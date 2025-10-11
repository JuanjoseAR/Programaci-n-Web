package com.unimag.web.api;

import com.unimag.web.api.dto.AirlineDto.*;
import com.unimag.web.services.AirlineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/airlines")
@RequiredArgsConstructor
@Validated
public class AirlineController {

    private final AirlineService service;

    @PostMapping
    public ResponseEntity<AirlineResponse> create(@Valid @RequestBody AirlineCreateRequest req,
                                                  UriComponentsBuilder uriBuilder) {
        var body = service.create(req);
        var location = uriBuilder.path("/api/airlines/{id}")
                .buildAndExpand(body.id())
                .toUri();
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirlineResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<AirlineResponse>> list(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @RequestParam(defaultValue = "name") String sort) {
        var pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AirlineResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody AirlineUpdateRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
