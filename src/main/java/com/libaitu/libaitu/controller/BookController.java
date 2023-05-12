package com.libaitu.libaitu.controller;

import com.libaitu.libaitu.dto.BookInfoRes;
import com.libaitu.libaitu.dto.GetBooksPaginationRes;
import com.libaitu.libaitu.entity.Categories;
import com.libaitu.libaitu.exception.NotFoundException;
import com.libaitu.libaitu.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

        List<Categories> res = bookService.getBookCategories();
        return ResponseEntity.ok(res);

    }

    @GetMapping("/getByCategories")
    public ResponseEntity getBooksByCategories(@RequestParam List<String> categories, @RequestParam int page, @RequestParam int size) {

        GetBooksPaginationRes res = bookService.getBooksByCategories(page, size, categories);
        return ResponseEntity.ok(res);

    }

    @GetMapping("/getInfo")
    public ResponseEntity getBookInfo(@RequestParam Integer bookId) throws NotFoundException {

        BookInfoRes res = bookService.getBookInfo(bookId);
        return ResponseEntity.ok(res);

    }



}
