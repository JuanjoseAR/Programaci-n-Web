package com.unimag.web.services;

import com.unimag.web.api.dto.AirportDto;
import java.util.List;

public interface AirportService {
    AirportDto.AirportResponse create(AirportDto.AirportCreateRequest request);
    AirportDto.AirportResponse findById(Long id);
    List<AirportDto.AirportResponse> findAll();
    void delete(Long id);
}