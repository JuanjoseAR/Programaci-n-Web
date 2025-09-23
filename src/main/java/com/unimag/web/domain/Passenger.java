package com.unimag.web.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name ="passenger")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Passenger {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, length = 50)
    private String fullName;
    private String email;

    @OneToOne @JoinColumn(name= "profaile_id")
    private PassengerProfile profile;
}
