package com.unimag.web.domain;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name ="passengerProfile")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PassengerProfile {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long  id;

    private String phone;

    private String countryCode;
}
