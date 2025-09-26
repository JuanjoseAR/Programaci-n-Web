package com.unimag.web.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
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

    @ManyToMany(mappedBy = "tags")
    @Builder.Default
    Set<Flight> flights = new HashSet<>();
}
