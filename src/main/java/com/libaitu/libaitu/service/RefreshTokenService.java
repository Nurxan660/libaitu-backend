package com.libaitu.libaitu.service;


import com.libaitu.libaitu.dto.TokenRefreshResponse;
import com.libaitu.libaitu.entity.RefreshToken;
import com.libaitu.libaitu.entity.User;
import com.libaitu.libaitu.exception.TokenExpiredException;
import com.libaitu.libaitu.exception.TokenNotFoundException;
import com.libaitu.libaitu.repository.RefreshTokenRepository;
import com.libaitu.libaitu.repository.UserRepository;
import com.libaitu.libaitu.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private JwtProvider jwtProvider;

    public String createRefreshToken(String username){
        User user=userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("barcode not found"));
        RefreshToken refreshToken=refreshTokenRepository.findByUserUserId(user.getUserId()).orElse(new RefreshToken());
        if(refreshToken.getToken()==null){
            refreshToken.setUser(user);
            refreshToken.setExpiredDate(LocalDateTime.now().plusHours(1));
            refreshToken.setToken(generateRefreshToken());
        }
        else{
            refreshToken.setToken(generateRefreshToken());
            refreshToken.setExpiredDate(LocalDateTime.now().plusHours(1));

        }
        refreshTokenRepository.save(refreshToken);

        return refreshToken.getToken();
    }
    public String generateRefreshToken(){
        return UUID.randomUUID().toString();
    }
    public TokenRefreshResponse checkExpiration(String token) throws TokenExpiredException, TokenNotFoundException {
        RefreshToken refreshToken=refreshTokenRepository.findByToken(token).orElseThrow(()->new TokenNotFoundException("token not found"));
        if(refreshToken.getExpiredDate().isBefore(LocalDateTime.now())){
            throw new TokenExpiredException("refresh token expired , please sign in ");
        }
        String newRefreshToken=generateRefreshToken();
        refreshToken.setToken(newRefreshToken);
        refreshToken.setExpiredDate(LocalDateTime.now().plusHours(1));
        refreshTokenRepository.save(refreshToken);
        String newJwtToken=jwtProvider.generateToken(refreshToken.getUser().getUsername());
        return new TokenRefreshResponse(newJwtToken,newRefreshToken);
    }
}
