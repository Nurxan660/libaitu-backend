package com.libaitu.libaitu.controller;

import com.libaitu.libaitu.dto.DoBookingRequest;
import com.libaitu.libaitu.dto.GetBooksPaginationRes;
import com.libaitu.libaitu.entity.BooksCategories;
import com.libaitu.libaitu.exception.NotFoundException;
import com.libaitu.libaitu.service.BookService;
import com.libaitu.libaitu.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BookController {

    @Autowired
    private BookService bookService;


    @GetMapping("/get")
    public ResponseEntity getBooks(@RequestParam int page, @RequestParam int size) {

        GetBooksPaginationRes res = bookService.getBooks(page, size);
        return ResponseEntity.ok(res);

    }

    @GetMapping("/getByPattern")
    public ResponseEntity getBooksByPattern(@RequestParam String pattern, @RequestParam int page, @RequestParam int size) {

        GetBooksPaginationRes res = bookService.getBooksBySearch(pattern,page, size);
        return ResponseEntity.ok(res);

    }

    @GetMapping("/getCategories")
    public ResponseEntity getBooksCategories() {

        List<BooksCategories> res = bookService.getBookCategories();
        return ResponseEntity.ok(res);

    }



}
