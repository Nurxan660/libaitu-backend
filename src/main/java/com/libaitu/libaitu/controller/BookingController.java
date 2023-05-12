package com.libaitu.libaitu.controller;

import com.libaitu.libaitu.dto.DoBookingRequest;
import com.libaitu.libaitu.dto.RegResponse;
import com.libaitu.libaitu.dto.RegistrationReq;
import com.libaitu.libaitu.exception.NotFoundException;
import com.libaitu.libaitu.service.BookingService;
import org.springframework.security.core.Authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/booking")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BookingController {

    @Autowired
    private BookingService bookingService;


    @PostMapping("/do")
    public ResponseEntity doBooking(@RequestBody DoBookingRequest req, Authentication authentication) throws NotFoundException {

        bookingService.doBooking(req, authentication);
        return ResponseEntity.ok("You have successfully made a booking");

    }
}
