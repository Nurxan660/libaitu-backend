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
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

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
                bookings.setBookingStatus(EBookingStatuses.PENDING);

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


}
