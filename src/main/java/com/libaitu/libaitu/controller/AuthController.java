package com.libaitu.libaitu.controller;


import com.libaitu.libaitu.dto.*;
import com.libaitu.libaitu.entity.User;
import com.libaitu.libaitu.exception.TokenExpiredException;
import com.libaitu.libaitu.exception.TokenNotFoundException;
import com.libaitu.libaitu.service.AuthService;
import com.libaitu.libaitu.service.EmailVerificationTokenService;
import com.libaitu.libaitu.service.RefreshTokenService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private EmailVerificationTokenService emailVerificationTokenService;
    private static final Logger log= LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/sign-in")
    public ResponseEntity signIn(@RequestBody LoginRequest loginRequest){
        log.info("sign in api");
        LoginResponse loginResponse = authService.signIn(loginRequest);
        return ResponseEntity.ok(loginResponse);

    }

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody RegistrationReq req){

        RegResponse regResponse=authService.registration(req);
        return ResponseEntity.ok(regResponse);

    }
    @DeleteMapping(value = "/logout")
    public ResponseEntity logout(@RequestParam String userId){
        log.info("logout api");
        authService.logout(userId);
        return ResponseEntity.ok("Successfully logout");
    }
    @PostMapping(value = "/refreshToken")
    public ResponseEntity refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) throws TokenExpiredException, TokenNotFoundException {
        log.info("refresh token api");
        TokenRefreshResponse tokenRefreshResponse=refreshTokenService.checkExpiration(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.ok(tokenRefreshResponse);

    }

    @PostMapping("/restorePassword")
    public ResponseEntity restorePassword(@RequestBody RestorePasswordRequest restorePasswordRequest){

        authService.restorePassword(restorePasswordRequest.getEmail());
        return ResponseEntity.ok("A link has been sent to your email");


    }

    @GetMapping("/confirmToken")
    public ResponseEntity confirmToken(@RequestParam String token) throws TokenExpiredException {
        emailVerificationTokenService.confirmTokenAndChangePassword(token);
        return ResponseEntity.ok("New password sended to your email");
    }
}
