package com.libaitu.libaitu.repository;

import com.libaitu.libaitu.entity.Bookings;
import com.libaitu.libaitu.entity.EBookingStatuses;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Bookings, Integer> {
    List<Bookings> findAllByUserUserIdAndBookingStatus(Integer userId, EBookingStatuses bookingStatus);
    List<Bookings> findAllByBookingStatus(EBookingStatuses bookingStatus);
}
