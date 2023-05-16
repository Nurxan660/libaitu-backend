package com.libaitu.libaitu.controller;

import com.libaitu.libaitu.dto.*;
import com.libaitu.libaitu.exception.BookAlreadyBookedException;
import com.libaitu.libaitu.exception.BookOutOfStockException;
import com.libaitu.libaitu.exception.NotFoundException;
import com.libaitu.libaitu.exception.StatusChangeException;
import com.libaitu.libaitu.service.BookingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BookingController {

    @Autowired
    private BookingService bookingService;


    @PostMapping("/do")
    public ResponseEntity doBooking(@RequestBody DoBookingRequest req, Authentication authentication) throws NotFoundException, BookOutOfStockException, BookAlreadyBookedException {

        bookingService.doBooking(req, authentication);
        return ResponseEntity.ok("You have successfully made a booking");

    }

    @DeleteMapping ("/cancel")
    public ResponseEntity cancelBooking(@RequestParam Integer bookingId) throws NotFoundException, BookOutOfStockException, BookAlreadyBookedException {

        bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok("You canceled booking");

    }

    @GetMapping ("/getByStatusAndEmail")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getBookingByStatusAndEmail(@RequestParam String email , @RequestParam List<String> statuses, int page, int size) {

        GetBookingsByStatusAndEmailRes res = bookingService.findStudentsBookingByEmailAndStatus(email, statuses, page, size);
        return ResponseEntity.ok(res);

    }

    @PutMapping ("/changeStatus")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity changeBookingStatus(@RequestBody ChangeBookingStatusReq req) throws NotFoundException, StatusChangeException {

        bookingService.changeBookingStatus(req);
        return ResponseEntity.ok("You have successfully changed the status!");

    }
}
