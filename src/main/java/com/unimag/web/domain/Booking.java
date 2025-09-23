package com.unimag.web.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "booking")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long  id;

    private OffsetDateTime createdAt;

    @ManyToOne @JoinColumn(name = "passenger_id", nullable = true)
    private Passenger passenger;

    @OneToMany(mappedBy = "booking", cascade=CascadeType.ALL)
    @Builder.Default
    private List<BookingItem> items = new ArrayList<>();

   public void addItem(BookingItem item) {
       this.items.add(item);
       item.setBooking(this);
   }



}
