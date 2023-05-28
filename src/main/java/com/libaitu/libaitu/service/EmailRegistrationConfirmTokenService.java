package com.libaitu.libaitu.service;


import com.libaitu.libaitu.dto.RegistrationReq;
import com.libaitu.libaitu.entity.EmailRegistrationConfirmToken;
import com.libaitu.libaitu.entity.EmailVerificationToken;
import com.libaitu.libaitu.entity.User;
import com.libaitu.libaitu.exception.InvalidEmailCodeException;
import com.libaitu.libaitu.repository.EmailRegistrationConfirmTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class EmailRegistrationConfirmTokenService {

    @Autowired
    private EmailRegistrationConfirmTokenRepository emailRegistrationConfirmTokenRepository;
    @Autowired
    MailService mailService;

    @Transactional
    public EmailRegistrationConfirmToken saveToken(RegistrationReq req, String token){
        if(emailRegistrationConfirmTokenRepository.existsByEmail(req.getEmail())) {
            List<EmailRegistrationConfirmToken> list = List.of(emailRegistrationConfirmTokenRepository.findByEmail(req.getEmail()).orElse(null));
            emailRegistrationConfirmTokenRepository.deleteInBatch(list);
        }
        EmailRegistrationConfirmToken emailToken=new EmailRegistrationConfirmToken(token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(2),
                req.getEmail());
        EmailRegistrationConfirmToken res=emailRegistrationConfirmTokenRepository.save(emailToken);
        return  res;
    }

    public String createToken(){
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }




}
