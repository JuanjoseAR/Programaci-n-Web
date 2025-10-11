package com.unimag.web.services.mapper;

import com.unimag.web.api.dto.SeatInventoryDto.SeatInventoryResponse;
import com.unimag.web.domain.SeatInventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SeatInventoryMapper {

    @Mapping(source = "cabin", target = "cabin")
    SeatInventoryResponse toResponse(SeatInventory inventory);

}
