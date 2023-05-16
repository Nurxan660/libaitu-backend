package com.libaitu.libaitu.controller;

import com.libaitu.libaitu.dto.*;
import com.libaitu.libaitu.entity.Categories;
import com.libaitu.libaitu.exception.BooksEqualException;
import com.libaitu.libaitu.exception.NotFoundException;
import com.libaitu.libaitu.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BookController {

    @Autowired
    private BookService bookService;


    @PostMapping("/save/book")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity saveBook(@RequestBody SaveBookReq req) {

        bookService.saveBook(req);
        return ResponseEntity.ok("You have successfully saved the book!");

    }

    @PutMapping("/change/book")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity changeBook(@RequestBody SaveBookReq req, @RequestParam Integer bookId) throws BooksEqualException, NotFoundException {

        bookService.changeBook(req, bookId);
        return ResponseEntity.ok("You have successfully changed the book!");

    }

    @PostMapping("/add/category")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity addCategory(@RequestBody AddCategoryReq req) {

        bookService.addCategory(req);
        return ResponseEntity.ok("You have successfully added the category!");

    }

    @DeleteMapping("/delete/book")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteBook(@RequestParam Integer bookId) {

        bookService.deleteBook(bookId);
        return ResponseEntity.ok("You have successfully deleted the book!");

    }

    @DeleteMapping("/delete/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteBookCategories(@RequestParam Integer bookId) {

        bookService.deleteBookCategory(bookId);
        return ResponseEntity.ok("You have successfully deleted the book categories!");

    }

    @PostMapping("/save/categoriesToBook")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity saveCategoriesToBook(@RequestBody SaveCategoriesToBookReq req) throws NotFoundException {

        bookService.saveCategoriesToBook(req.getCategories(),req.getBookId());
        return ResponseEntity.ok("You have successfully added categories to book!");

    }

    @GetMapping("/get")
    public ResponseEntity getBooks(@RequestParam int page, @RequestParam int size) {

        GetBooksPaginationRes res = bookService.getBooks(page, size);
        return ResponseEntity.ok(res);

    }

    @GetMapping("/getRecommended")
    public ResponseEntity getRecommendedBooks(@RequestParam int page, @RequestParam int size) {

        GetBooksPaginationRes res = bookService.getRecommendedBooks(page, size);
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

    @GetMapping("/get/requested")
    public ResponseEntity getRequestedBooks(Authentication authentication) throws NotFoundException {

        List<RequestedBooksRes> res= bookService.getRequestedBooks(authentication);
        return ResponseEntity.ok(res);

    }

    @GetMapping("/get/used")
    public ResponseEntity getUsedBooks(Authentication authentication) throws NotFoundException {

        List<UsedBookRes> res= bookService.getUsedBooks(authentication);
        return ResponseEntity.ok(res);

    }
    @GetMapping("/get/completed")
    public ResponseEntity getCompletedBooks(@RequestParam  int page, @RequestParam  int size, Authentication authentication) {

        CompletedBooksPaginationRes res= bookService.getCompletedBooksOfUser(authentication, page, size);
        return ResponseEntity.ok(res);

    }




}
