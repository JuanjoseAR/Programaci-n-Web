package com.unimag.web.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name= "airline")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Airline {
    @Id @GeneratedValue( strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, nullable = false)
    private String code;

    private String name;

    @OneToMany(mappedBy = "airline", cascade = CascadeType.ALL)
    @Builder.Default
    List<Flight> flights = new ArrayList<>();

    public void addFlight(Flight flight) {
        flights.add(flight);
        flight.setAirline(this);
    }
}
