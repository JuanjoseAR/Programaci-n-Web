package com.unimag.web.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name= "tag")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Tag {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToMany
    @JoinTable(name = "flight_tags",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "flight_id"))
    Set<Flight> flights;
}
