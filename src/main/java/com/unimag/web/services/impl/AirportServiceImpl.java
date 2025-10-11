package com.unimag.web.services.impl;

import com.unimag.web.api.dto.AirportDto;
import com.unimag.web.exception.NotFoundException;
import com.unimag.web.repositories.AirportRepository;
import com.unimag.web.services.AirportService;
import com.unimag.web.services.mapper.AirportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AirportServiceImpl implements AirportService {

    private final AirportRepository airportRepository;

    @Override
    public AirportDto.AirportResponse create(AirportDto.AirportCreateRequest request) {
        return AirportMapper.toResponse(airportRepository.save(AirportMapper.toEntity(request)));
    }

    @Override
    @Transactional(readOnly = true)
    public AirportDto.AirportResponse findById(Long id) {
        return airportRepository.findById(id)
                .map(AirportMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Airport con id " + id + " no encontrado."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AirportDto.AirportResponse> findAll() {
        return airportRepository.findAll().stream()
                .map(AirportMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {
        airportRepository.deleteById(id);
    }
}