package com.libaitu.libaitu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="bookings")
@Getter
@Setter
@NoArgsConstructor
public class Bookings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer bookingId;
    @ManyToOne
    @JoinColumn(name="book_id")
    private Books books;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    private Integer amountOfDay;

    @Enumerated(EnumType.STRING)
    private EBookingStatuses bookingStatus;



}
