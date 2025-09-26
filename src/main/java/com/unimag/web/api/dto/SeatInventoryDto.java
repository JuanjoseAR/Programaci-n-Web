package com.unimag.web.api.dto;

import java.io.Serializable;

public class SeatInventoryDto {

    public record SeatInventoryResponse(
            Long id,
            String cabin,
            Integer totalSeats,
            Integer availableSeats
    ) implements Serializable {}
}
