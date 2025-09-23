package com.unimag.web.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.HashSet;
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

    @ManyToMany()
    @JoinTable(name = "flight_tags",
            joinColumns = @JoinColumn(name = "flight_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();

    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getFlights().add(this);
    }
}
