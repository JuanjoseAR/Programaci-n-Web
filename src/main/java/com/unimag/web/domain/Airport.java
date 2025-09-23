package com.unimag.web.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="airport")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Airport {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, nullable = false)
    private String code;

    private String name;

    private String city;
}
