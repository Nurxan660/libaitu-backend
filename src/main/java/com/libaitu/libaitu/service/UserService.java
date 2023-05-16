package com.libaitu.libaitu.service;


import com.libaitu.libaitu.dto.UserInfoRes;
import com.libaitu.libaitu.entity.Bookings;
import com.libaitu.libaitu.entity.EBookingStatuses;
import com.libaitu.libaitu.entity.User;
import com.libaitu.libaitu.exception.NotFoundException;
import com.libaitu.libaitu.repository.BookingRepository;
import com.libaitu.libaitu.repository.UserRepository;
import com.libaitu.libaitu.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;

    public UserInfoRes getUserInfo(Authentication authentication) throws NotFoundException {
        UserDetailsImpl userDetails  = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findById(userDetails.getUserId()).orElseThrow(()->new NotFoundException("user not found"));
        List<Bookings> bookingsList = bookingRepository.findAllByUserUserIdAndBookingStatus(user.getUserId(), EBookingStatuses.COMPLETED);
        long total = bookingsList.stream().mapToLong((d)-> Duration.between(d.getBookingCompletedTime(),d.getBookingTime().plusDays(d.getAmountOfDay())).getSeconds()).sum();
        UserInfoRes res = new UserInfoRes();
        res.setUserName(user.getUsername());
        res.setEmail(user.getEmail());
        res.setFullName(user.getFullName());
        res.setTotalDayOfReturn(total);
        return res;

    }


}
