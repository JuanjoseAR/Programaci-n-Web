package com.unimag.web.services;

import com.unimag.web.api.dto.AirlineDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AirlineService {
    AirlineDto.AirlineResponse create(AirlineDto.AirlineCreateRequest request);
    AirlineDto.AirlineResponse findById(Long id);
    Page<AirlineDto.AirlineResponse> findAll(Pageable pageable);
    AirlineDto.AirlineResponse update(Long id, AirlineDto.AirlineUpdateRequest req);
    void delete(Long id);
}
