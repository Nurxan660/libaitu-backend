package com.libaitu.libaitu.service;


import com.libaitu.libaitu.dto.ChangeBookingStatusReq;
import com.libaitu.libaitu.dto.CompletedBooksPaginationRes;
import com.libaitu.libaitu.dto.DoBookingRequest;
import com.libaitu.libaitu.dto.GetBookingsByStatusAndEmailRes;
import com.libaitu.libaitu.dto.pojo.BookingByStatusAndEmail;
import com.libaitu.libaitu.dto.pojo.CompletedUserBooks;
import com.libaitu.libaitu.entity.*;
import com.libaitu.libaitu.exception.*;
import com.libaitu.libaitu.repository.BookRepository;
import com.libaitu.libaitu.repository.BookingRepository;
import com.libaitu.libaitu.repository.UserRepository;
import com.libaitu.libaitu.security.UserDetailsImpl;
import jakarta.persistence.*;

import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    private static final Logger log= LoggerFactory.getLogger(BookingService.class);


    public void doBooking(DoBookingRequest doBookingRequest, Authentication authentication) throws NotFoundException, BookOutOfStockException, BookAlreadyBookedException, BookBookingNoAccess {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findById(userDetails.getUserId()).orElseThrow(()->new NotFoundException("user not found"));
        Books books = bookRepository.findById(doBookingRequest.getBookId()).orElseThrow(()->new NotFoundException("book not found"));

        List<Bookings> bookingsList = bookingRepository.findAllByUserUserIdAndBookingStatus(userDetails.getUserId(), EBookingStatuses.COMPLETED);
        long total = bookingsList.stream().mapToLong((d)-> Duration.between(d.getBookingCompletedTime(),d.getBookingTime().plusDays(d.getAmountOfDay())).toDays()).sum();

        List<EBookingStatuses> statuses = List.of(EBookingStatuses.IN_USE, EBookingStatuses.ALLEGED_TAKING);
            if (books.getAmountOfBooks() == 0) {
                throw new BookOutOfStockException("Books are out of stock!");
            }
            else if(Math.abs(total)>30) {
                throw new BookBookingNoAccess("You can't booking the book because you're over 30 late!");

            }
            else if(bookingRepository.existsByBooksBookIdAndUserUserIdAndBookingStatusIn(books.getBookId(), userDetails.getUserId(), statuses )) {
                throw new BookAlreadyBookedException("You have already booked the book");
            }
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
        Books book = booking.getBooks();
        book.setAmountOfBooks(book.getAmountOfBooks()+1);
        booking.setBookingStatus(EBookingStatuses.CANCELED);
            bookingRepository.delete(booking);
            bookRepository.save(book);

    }

    public GetBookingsByStatusAndEmailRes findStudentsBookingByEmailAndStatus(String email, List<String> status, int page , int size) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        List<EBookingStatuses> statuses = new ArrayList<>();
        status.forEach((d)->{
            if(d.equals("In use")) {
                statuses.add(EBookingStatuses.IN_USE);
            }
            if(d.equals("Alleged taking")){
                statuses.add(EBookingStatuses.ALLEGED_TAKING);
            }
            if(d.equals("Completed")){
                statuses.add(EBookingStatuses.COMPLETED);
            }
        });

        Pageable pageable = PageRequest.of(page, size);
        GetBookingsByStatusAndEmailRes res = new GetBookingsByStatusAndEmailRes();
        Page<Bookings> bookingsList = bookingRepository.findAllByUserEmailAndBookingStatusIn(email,statuses, pageable);
        List<BookingByStatusAndEmail> bookingByStatusAndEmails = bookingsList.stream().map((d)->{
            BookingByStatusAndEmail booking = new BookingByStatusAndEmail();
            booking.setBookingId(d.getBookingId());
            booking.setBookName(d.getBooks().getBookName());
            booking.setEmail(d.getUser().getEmail());
            booking.setBookingStatus(d.getBookingStatus());
            booking.setPhoneNumber(d.getPhoneNumber());
            booking.setFullName(d.getUser().getFullName());
            booking.setRemainTime(Duration.between(LocalDateTime.now(), d.getBookingTime().plusDays(d.getAmountOfDay())).getSeconds());
            return booking;
        }).collect(Collectors.toList());

        res.setContent(bookingByStatusAndEmails);
        res.setTotalPages(bookingsList.getTotalPages());
        res.setTotalElements((int) bookingsList.getTotalElements());
        return res;
    }


    public GetBookingsByStatusAndEmailRes findStudentsBookingByStatus( int page , int size) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);


        Pageable pageable = PageRequest.of(page, size);
        GetBookingsByStatusAndEmailRes res = new GetBookingsByStatusAndEmailRes();
        Page<Bookings> bookingsList = bookingRepository.findAllByBookingStatus(EBookingStatuses.ALLEGED_TAKING, pageable);
        List<BookingByStatusAndEmail> bookingByStatusAndEmails = bookingsList.stream().map((d)->{
            BookingByStatusAndEmail booking = new BookingByStatusAndEmail();
            booking.setBookingId(d.getBookingId());
            booking.setBookName(d.getBooks().getBookName());
            booking.setEmail(d.getUser().getEmail());
            booking.setBookingStatus(d.getBookingStatus());
            booking.setPhoneNumber(d.getPhoneNumber());
            booking.setFullName(d.getUser().getFullName());
            booking.setRemainTime(Duration.between(LocalDateTime.now(), d.getBookingTime().plusDays(d.getAmountOfDay())).getSeconds());
            return booking;
        }).collect(Collectors.toList());

        res.setContent(bookingByStatusAndEmails);

        res.setTotalPages(bookingsList.getTotalPages());
        res.setTotalElements((int) bookingsList.getTotalElements());

        return res;
    }

    public void changeBookingStatus(ChangeBookingStatusReq req) throws NotFoundException, StatusChangeException {
        Bookings bookings = bookingRepository.findById(req.getBookingId()).orElseThrow(()->new NotFoundException("Booking not found"));
        switch (req.getStatus()) {
            case "Canceled":

                bookingRepository.deleteInBatch(List.of(bookings));
                break;
            case "In use":
                bookings.setBookingStatus(EBookingStatuses.IN_USE);
                bookings.setBookingTime(LocalDateTime.now());
                break;
            case "Completed":
                if(bookings.getBookingStatus().equals(EBookingStatuses.ALLEGED_TAKING)) {
                    throw new StatusChangeException("You cannot change status from alleged taking to completed");
                } else {

                    bookings.setBookingStatus(EBookingStatuses.COMPLETED);
                    bookings.setBookingCompletedTime(LocalDateTime.now());
                    bookings.setShowInHistory(true);
                }

                break;
            case "Alleged taking":
                bookings.setBookingStatus(EBookingStatuses.ALLEGED_TAKING);
                break;
        }
        bookingRepository.save(bookings);
    }













}
