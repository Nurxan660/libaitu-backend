package com.libaitu.libaitu.exception.handler;


import com.libaitu.libaitu.dto.ResponseMessage;
import com.libaitu.libaitu.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomAdvice {


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseMessage> handleLoginException(BadCredentialsException e){
        return ResponseEntity.status(401).body(new ResponseMessage("password or nickname incorrect"));
    }

    @ExceptionHandler({TokenExpiredException.class, TokenNotFoundException.class})
    public ResponseEntity<ResponseMessage> handleRefreshTokenException(Exception e){
        ResponseMessage exception=new ResponseMessage(e.getMessage());
        return ResponseEntity.status(401).body(exception);
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseMessage> handleNotFoundException(Exception e){
        ResponseMessage exception=new ResponseMessage(e.getMessage());
        return ResponseEntity.status(404).body(exception);
    }

    @ExceptionHandler({BookOutOfStockException.class, BookAlreadyBookedException.class, BooksEqualException.class, StatusChangeException.class})
    public ResponseEntity<ResponseMessage> bookOutOfStockException(Exception e){
        ResponseMessage exception=new ResponseMessage(e.getMessage());
        return ResponseEntity.status(409).body(exception);
    }

    @ExceptionHandler(PasswordMatcherException.class)
    public ResponseEntity<ResponseMessage> handlePass(PasswordMatcherException e){
        ResponseMessage exception=new ResponseMessage(e.getMessage());
        return ResponseEntity.status(400).body(exception);
    }

    @ExceptionHandler({BookBookingNoAccess.class, EmailAlreadyExistException.class, UsernameAlreadyExistException.class, InvalidEmailCodeException.class})
    public ResponseEntity<ResponseMessage> handleNoAccess(Exception e){
        ResponseMessage exception=new ResponseMessage(e.getMessage());
        return ResponseEntity.status(400).body(exception);
    }



}
