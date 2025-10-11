package com.unimag.web.services;

import com.unimag.web.api.dto.BookingDto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookingService {
    BookingResponse create(BookingCreateRequest request);

    BookingResponse findById(Long id);

    Page<BookingResponse> findAll(Pageable pageable);

    BookingResponse update(Long id, BookingCreateRequest request);

    void delete(Long id);

    Page<BookingResponse> findAllByPassenger(Long passengerId, Pageable pageable);

}
