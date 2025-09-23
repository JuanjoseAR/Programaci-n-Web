package com.unimag.web.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name="bookingitem")
@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingItem {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Cabin cabin;

    private BigDecimal price;

    private Integer segmentOrder;

    @ManyToOne @JoinColumn(name= "booking_id", unique = false)
    private Booking booking;

    @ManyToOne @JoinColumn(name="flight_id")
    private Flight flight;
}
