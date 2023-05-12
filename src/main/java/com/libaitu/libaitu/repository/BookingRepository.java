package com.libaitu.libaitu.repository;

import com.libaitu.libaitu.entity.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Bookings, Integer> {
}
