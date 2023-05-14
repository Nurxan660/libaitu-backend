package com.libaitu.libaitu.service;


import com.libaitu.libaitu.dto.DoBookingRequest;
import com.libaitu.libaitu.entity.*;
import com.libaitu.libaitu.exception.BookAlreadyBookedException;
import com.libaitu.libaitu.exception.BookOutOfStockException;
import com.libaitu.libaitu.exception.NotFoundException;
import com.libaitu.libaitu.repository.BookRepository;
import com.libaitu.libaitu.repository.BookingRepository;
import com.libaitu.libaitu.repository.UserRepository;
import com.libaitu.libaitu.security.UserDetailsImpl;
import jakarta.persistence.*;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@EnableScheduling
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    private static final Logger log= LoggerFactory.getLogger(BookingService.class);


    public void doBooking(DoBookingRequest doBookingRequest, Authentication authentication) throws NotFoundException, BookOutOfStockException, BookAlreadyBookedException {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findById(userDetails.getUserId()).orElseThrow(()->new NotFoundException("user not found"));
        Books books = bookRepository.findById(doBookingRequest.getBookId()).orElseThrow(()->new NotFoundException("book not found"));
        try {
            if (books.getAmountOfBooks() != 0) {

                Bookings bookings = new Bookings();
                bookings.setBooks(books);
                bookings.setUser(user);
                bookings.setPhoneNumber(doBookingRequest.getPhoneNumber());
                bookings.setAmountOfDay(doBookingRequest.getAmountOfDay());
                bookings.setBookingStatus(EBookingStatuses.ALLEGED_TAKING);
                bookings.setBookingTime(LocalDateTime.now());
                bookingRepository.save(bookings);
                books.setAmountOfBooks(books.getAmountOfBooks() - 1);
                bookRepository.save(books);
            } else {
                throw new BookOutOfStockException("Books are out of stock!");
            }
        } catch (DataIntegrityViolationException e) {
                String message = "You have already booked this book!";
                throw new BookAlreadyBookedException(message);

        }

    }

    @Scheduled(fixedDelay = 60000)
    public void checkBookReservationTime() {
        List<Bookings> bookingsList = bookingRepository.findAllByBookingStatus(EBookingStatuses.ALLEGED_TAKING);
        if(bookingsList.size()!=0) {
            for (Bookings reservation : bookingsList) {
                LocalDateTime reservationEndTime = reservation.getBookingTime().plusHours(2);
                if (LocalDateTime.now().isAfter(reservationEndTime)) {
                    reservation.setBookingStatus(EBookingStatuses.CANCELED);
                    bookingRepository.delete(reservation);
                }
            }

        }
    }


    public void cancelBooking(Integer bookingId) throws NotFoundException {
        Bookings booking = bookingRepository.findById(bookingId).orElseThrow(()->new NotFoundException("booking not found"));
        booking.setBookingStatus(EBookingStatuses.CANCELED);
            bookingRepository.delete(booking);

    }



}
