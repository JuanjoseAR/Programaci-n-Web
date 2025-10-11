package com.unimag.web.services;

import com.unimag.web.api.dto.SeatInventoryDto;
import java.util.List;

public interface SeatInventoryService {
    SeatInventoryDto.SeatInventoryResponse findById(Long id);
    List<SeatInventoryDto.SeatInventoryResponse> findAll();
}