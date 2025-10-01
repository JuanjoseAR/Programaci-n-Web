package com.unimag.web.services.impl;

import com.unimag.web.api.dto.AirlineDto;
import com.unimag.web.exception.NotFoundException;
import com.unimag.web.services.mapper.AirlineMapper;
import com.unimag.web.repositories.AirlineRepository;
import com.unimag.web.services.AirlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AirlineServiceImpl implements AirlineService {

    private final AirlineRepository airlineRepository;
    private final AirlineMapper airlineMapper;

    @Override
    public AirlineDto.AirlineResponse create(AirlineDto.AirlineCreateRequest request) {
        return airlineMapper.toResponse(airlineRepository.save(airlineMapper.toEntity(request)));
    }

    @Override
    @Transactional(readOnly = true)
    public AirlineDto.AirlineResponse findById(Long id) {
        return airlineRepository.findById(id)
                .map(airlineMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Airline con id " + id + " no encontrada."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AirlineDto.AirlineResponse> findAll() {
        return airlineRepository.findAll().stream()
                .map(airlineMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {
        airlineRepository.deleteById(id);
    }
}
