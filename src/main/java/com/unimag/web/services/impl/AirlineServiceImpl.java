package com.unimag.web.services.impl;

import com.unimag.web.api.dto.AirlineDto;
import com.unimag.web.exception.NotFoundException;
import com.unimag.web.repositories.AirlineRepository;
import com.unimag.web.services.AirlineService;
import com.unimag.web.services.mapper.AirlineMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Page<AirlineDto.AirlineResponse> findAll(Pageable pageable) {
        return airlineRepository.findAll(pageable)
                .map(airlineMapper::toResponse);
    }

    @Override
    public AirlineDto.AirlineResponse update(Long id, AirlineDto.AirlineUpdateRequest req) {
        return null;
    }


    @Override
    public void delete(Long id) {
        airlineRepository.deleteById(id);
    }
}
