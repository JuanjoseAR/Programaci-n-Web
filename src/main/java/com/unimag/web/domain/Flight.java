package com.unimag.web.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(name ="flight")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flight {

    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    String flightNumber;

    OffsetDateTime departureTime;
    OffsetDateTime arrivalTime;
    @ManyToOne @JoinColumn(name = "airline_id")
    private Airline airline;

    @ManyToOne @JoinColumn(name = "airport_origin_id")
    private Airport origin;

    @ManyToOne @JoinColumn(name = "airport_destination_id")
    private Airport destination;

    @ManyToMany(mappedBy = "flight")
    private Set<Tag> tags;

}
