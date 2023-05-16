package com.libaitu.libaitu.controller;


import com.libaitu.libaitu.dto.SaveBookReq;
import com.libaitu.libaitu.dto.UserInfoRes;
import com.libaitu.libaitu.exception.BooksEqualException;
import com.libaitu.libaitu.exception.NotFoundException;
import com.libaitu.libaitu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/getInfo")
    public ResponseEntity getInfo(Authentication authentication) throws NotFoundException {

        UserInfoRes res = userService.getUserInfo(authentication);
        return ResponseEntity.ok(res);

    }





}
