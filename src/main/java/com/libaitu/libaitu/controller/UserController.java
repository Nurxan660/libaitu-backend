package com.libaitu.libaitu.controller;


import com.libaitu.libaitu.dto.ChangePasswordReq;
import com.libaitu.libaitu.dto.SaveBookReq;
import com.libaitu.libaitu.dto.UserInfoRes;
import com.libaitu.libaitu.dto.UserInfoResPagination;
import com.libaitu.libaitu.exception.BooksEqualException;
import com.libaitu.libaitu.exception.NotFoundException;
import com.libaitu.libaitu.exception.PasswordMatcherException;
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


    @GetMapping("/getUserInfoByPattern")
    public ResponseEntity getInfoByPattern(@RequestParam String pattern, int page, int size) throws NotFoundException {

        UserInfoResPagination res = userService.getUserInfoByPattern(pattern, page, size);
        return ResponseEntity.ok(res);

    }

    @PutMapping("/change/password")
    public ResponseEntity changePassword(@RequestBody ChangePasswordReq req, Authentication authentication) throws NotFoundException, PasswordMatcherException {

        userService.changePassword(authentication,req.getOldPassword(), req.getNewPassword());
        return ResponseEntity.ok("You successfully changed the password!");

    }





}
