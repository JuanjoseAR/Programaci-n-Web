package com.unimag.web.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name ="seatinventory")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatInventory {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @Enumerated(EnumType.STRING)
    private Cabin cabin;

    private int totalSeats;

    private int availableSeats;

    @ManyToOne @JoinColumn(name= "flight_id", nullable = false)
    private Flight flight;

}
