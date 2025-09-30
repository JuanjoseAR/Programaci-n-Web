package com.unimag.web.services.impl;

import com.unimag.web.api.dto.SeatInventoryDto;
import com.unimag.web.domain.SeatInventory;
import com.unimag.web.exception.NotFoundException;
import com.unimag.web.repositories.SeatInventoryRepository;
import com.unimag.web.services.SeatInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SeatInventoryServiceImpl implements SeatInventoryService {

    private final SeatInventoryRepository seatInventoryRepository;

    @Override
    @Transactional(readOnly = true)
    public SeatInventoryDto.SeatInventoryResponse findById(Long id) {
        return seatInventoryRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("SeatInventory con id " + id + " no encontrado."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeatInventoryDto.SeatInventoryResponse> findAll() {
        return seatInventoryRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private SeatInventoryDto.SeatInventoryResponse toResponse(SeatInventory inventory) {
        if (inventory == null) {
            return null;
        }
        return new SeatInventoryDto.SeatInventoryResponse(
                inventory.getId(),
                inventory.getCabin().name(),
                inventory.getTotalSeats(),
                inventory.getAvailableSeats()
        );
    }
}