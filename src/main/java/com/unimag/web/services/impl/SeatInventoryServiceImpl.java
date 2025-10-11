package com.unimag.web.services.impl;

import com.unimag.web.api.dto.SeatInventoryDto.SeatInventoryResponse;
import com.unimag.web.domain.SeatInventory;
import com.unimag.web.exception.NotFoundException;
import com.unimag.web.repositories.SeatInventoryRepository;
import com.unimag.web.services.SeatInventoryService;
import com.unimag.web.services.mapper.SeatInventoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SeatInventoryServiceImpl implements SeatInventoryService {

    private final SeatInventoryRepository seatInventoryRepository;
    private final SeatInventoryMapper seatInventoryMapper;

    @Override
    @Transactional(readOnly = true)
    public SeatInventoryResponse findById(Long id) {
        return seatInventoryRepository.findById(id)
                .map(seatInventoryMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("SeatInventory con id " + id + " no encontrado."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeatInventoryResponse> findAll() {
        return seatInventoryRepository.findAll().stream()
                .map(seatInventoryMapper::toResponse)
                .toList();
    }
}
