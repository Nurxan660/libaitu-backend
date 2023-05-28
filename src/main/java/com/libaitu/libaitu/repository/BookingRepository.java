package com.libaitu.libaitu.repository;

import com.libaitu.libaitu.entity.Bookings;
import com.libaitu.libaitu.entity.EBookingStatuses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Bookings, Integer> {
    List<Bookings> findAllByUserUserIdAndBookingStatus(Integer userId, EBookingStatuses bookingStatus);
    Page<Bookings> findAllByUserUserIdAndBookingStatusAndShowInHistory(Integer userId, EBookingStatuses bookingStatus, boolean showInHistory, Pageable pageable);

    List<Bookings> findAllByBookingStatus(EBookingStatuses bookingStatus);

    Page<Bookings> findAllByBookingStatus(EBookingStatuses bookingStatus, Pageable pageable);

    Page<Bookings> findAllByUserEmailAndBookingStatusIn(String email, List<EBookingStatuses> bookingStatus, Pageable pageable);


    boolean existsByBooksBookIdAndUserUserIdAndBookingStatusIn(Integer bookId, Integer userId,  List<EBookingStatuses> bookingStatus);
}
