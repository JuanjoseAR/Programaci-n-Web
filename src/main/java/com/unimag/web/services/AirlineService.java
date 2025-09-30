package com.unimag.web.services;

import com.unimag.web.api.dto.AirlineDto;
import java.util.List;

public interface AirlineService {
    AirlineDto.AirlineResponse create(AirlineDto.AirlineCreateRequest request);
    AirlineDto.AirlineResponse findById(Long id);
    List<AirlineDto.AirlineResponse> findAll();
    void delete(Long id);
}
