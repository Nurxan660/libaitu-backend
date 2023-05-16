package com.libaitu.libaitu.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"book_id", "user_id"})})
@Getter
@Setter
@NoArgsConstructor
public class Bookings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer bookingId;
    @ManyToOne()
    @JoinColumn(name="book_id")
    private Books books;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    private Integer amountOfDay;
    private String phoneNumber;
    private LocalDateTime bookingTime;
    @Enumerated(EnumType.STRING)
    private EBookingStatuses bookingStatus;



}
