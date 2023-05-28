package com.libaitu.libaitu.service;


import com.libaitu.libaitu.dto.UserInfoRes;
import com.libaitu.libaitu.dto.UserInfoResPagination;
import com.libaitu.libaitu.entity.Bookings;
import com.libaitu.libaitu.entity.EBookingStatuses;
import com.libaitu.libaitu.entity.User;
import com.libaitu.libaitu.exception.NotFoundException;
import com.libaitu.libaitu.exception.PasswordMatcherException;
import com.libaitu.libaitu.repository.BookingRepository;
import com.libaitu.libaitu.repository.UserRepository;
import com.libaitu.libaitu.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public UserInfoResPagination getUserInfoByPattern(String pattern, int page , int size) {
        Pageable pageable = PageRequest.of(page, size);
        UserInfoResPagination res = new UserInfoResPagination();
        Page<User> users =userRepository.findByPattern(pattern, pageable);
        List<UserInfoRes> userInfoRes = users.stream().map((v)->{
            List<Bookings> bookingsList = bookingRepository.findAllByUserUserIdAndBookingStatus(v.getUserId(), EBookingStatuses.COMPLETED);
            long total = bookingsList.stream().mapToLong((d)-> Duration.between(d.getBookingCompletedTime(),d.getBookingTime().plusDays(d.getAmountOfDay())).getSeconds()).sum();
            UserInfoRes infoRes = new UserInfoRes();
            infoRes.setUserName(v.getUsername());
            infoRes.setEmail(v.getEmail());
            infoRes.setFullName(v.getFullName());
            infoRes.setTotalDayOfReturn(total);
            return infoRes;
        }).collect(Collectors.toList());
        res.setContent(userInfoRes);
        res.setTotalPages(users.getTotalPages());
        return res;
    }

    public void changePassword(Authentication authentication,String oldPassword,String newPassword) throws NotFoundException, PasswordMatcherException {
        UserDetailsImpl userDetails= (UserDetailsImpl) authentication.getPrincipal();

        if(passwordEncoder.matches(oldPassword,userDetails.getPassword())){
            User user=userRepository.findById(userDetails.getUserId()).orElseThrow(()->new NotFoundException("not found"));
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }
        else{
            throw new PasswordMatcherException("old password is incorrect");
        }
    }


}
