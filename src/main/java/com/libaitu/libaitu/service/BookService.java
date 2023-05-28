package com.libaitu.libaitu.service;

import com.libaitu.libaitu.compositeKey.BookCategoryKey;
import com.libaitu.libaitu.dto.*;
import com.libaitu.libaitu.dto.pojo.CompletedUserBooks;
import com.libaitu.libaitu.entity.*;
import com.libaitu.libaitu.exception.BooksEqualException;
import com.libaitu.libaitu.exception.NotFoundException;
import com.libaitu.libaitu.repository.BookCategoryRepository;
import com.libaitu.libaitu.repository.BookingRepository;
import com.libaitu.libaitu.repository.CategoryRepository;
import com.libaitu.libaitu.repository.BookRepository;
import com.libaitu.libaitu.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BookCategoryRepository bookCategoryRepository;
    @Autowired
    private BookingRepository bookingRepository;


    public void saveBook(SaveBookReq req) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Books book= modelMapper.map(req, Books.class);
        book.setRecommended(req.isRecommended());
        book.setNew(req.isNews());
        bookRepository.save(book);
        List<BookCategory> bookCategoryList=new ArrayList<>();
        req.getCategories().forEach((d)->{
            try {
                BookCategory bookCategory = new BookCategory();
                Categories category = categoryRepository.findByBookCategoryName(d).orElseThrow(()->new NotFoundException("not found"));
                BookCategoryKey bookCategoryKey = new BookCategoryKey();
                bookCategoryKey.setBookId(book.getBookId());
                bookCategoryKey.setCategoryId(category.getBookCategoryId());
                bookCategory.setBooks(book);
                bookCategory.setCategories(category);
                bookCategory.setBookCategoryKey(bookCategoryKey);
                bookCategoryList.add(bookCategory);

            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        bookCategoryRepository.saveAll(bookCategoryList);
    }

    public void changeBook(SaveBookReq req, Integer bookId) throws NotFoundException, BooksEqualException {


        Books book = bookRepository.findById(bookId).orElseThrow(()->new NotFoundException("book not found"));

        List<String> mappedCategories = book.getBookCategories().stream().map(d->d.getCategories().getBookCategoryName()).collect(Collectors.toList());
        Arrays.sort(mappedCategories.toArray());
        Arrays.sort(req.getCategories().toArray());
        boolean isCategoriesEquals = Arrays.equals(new List[]{req.getCategories()}, new List[]{mappedCategories});;

        if(book.getBookName().equals(req.getBookName())&&book.getBookAuthor().equals(req.getBookAuthor())
        &&book.getYearOfPublishing().equals(req.getYearOfPublishing())&&book.getRating().equals(req.getRating())
        &&book.getAmountOfBooks().equals(req.getAmountOfBooks())&&book.getBookDescription().equals(req.getBookDescription())
        &&book.getBookImageUrl().equals(req.getBookImageUrl())&&isCategoriesEquals&&book.isRecommended()==req.isRecommended()&&book.isNew()==req.isNews()) {
            throw new BooksEqualException("You haven't changed anything!");
        }
        else {

            book.setBookName(req.getBookName());
            book.setRecommended(req.isRecommended());
            book.setNew(req.isNews());
            book.setBookAuthor(req.getBookAuthor());
            book.setYearOfPublishing(req.getYearOfPublishing());
            book.setRating(req.getRating());
            book.setAmountOfBooks(req.getAmountOfBooks());
            book.setBookDescription(req.getBookDescription());
            book.setBookImageUrl(req.getBookImageUrl());

            bookRepository.save(book);

        }

    }

    public void saveCategoriesToBook(List<String> categories, Integer bookId) throws NotFoundException {
        Books book = bookRepository.findById(bookId).orElseThrow(()->new NotFoundException("book not found"));
        List<BookCategory> bookCategoryList=new ArrayList<>();

        categories.forEach((d)->{
            try {
                BookCategory bookCategory = new BookCategory();
                Categories category = categoryRepository.findByBookCategoryName(d).orElseThrow(()->new NotFoundException("not found"));
                BookCategoryKey bookCategoryKey = new BookCategoryKey();
                bookCategoryKey.setBookId(bookId);
                bookCategoryKey.setCategoryId(category.getBookCategoryId());
                bookCategory.setBooks(book);
                bookCategory.setCategories(category);
                bookCategory.setBookCategoryKey(bookCategoryKey);
                bookCategoryList.add(bookCategory);

            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
            book.setBookCategories(bookCategoryList);
            bookRepository.save(book);
        });
    }

    public void addCategory(AddCategoryReq req) {
        Categories categories = new Categories();
        categories.setBookCategoryName(req.getCategoryName());
        categoryRepository.save(categories);

    }

    @Transactional
    public void deleteBookCategory(Integer bookId) {
        bookCategoryRepository.deleteByBooksBookId(bookId);
    }
    public GetBooksPaginationRes getBooks (int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Books> books = bookRepository.findAll(pageable);
        GetBooksPaginationRes res = modelMapper.map(books,GetBooksPaginationRes.class);
        return res;

    }

    public GetBooksPaginationRes getRecommendedBooks (int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Books> books = bookRepository.findAllByIsRecommended(true,pageable);
        GetBooksPaginationRes res = modelMapper.map(books,GetBooksPaginationRes.class);
        return res;

    }

    public GetBooksPaginationRes getNewBooks (int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Books> books = bookRepository.findAllByIsNew(true,pageable);
        GetBooksPaginationRes res = modelMapper.map(books,GetBooksPaginationRes.class);
        return res;

    }


    public GetBooksPaginationRes getBooksBySearch(String pattern,int page, int size) {
        Pageable pageable= PageRequest.of(page, size);
        Page<Books> books =bookRepository.findByPattern(pattern, pageable);
        GetBooksPaginationRes res = modelMapper.map(books,GetBooksPaginationRes.class);
        return res;
    }

    public List<Categories> getBookCategories() {
        List<Categories> res= (List<Categories>) categoryRepository.findAll();
        return res;
    }

    public GetBooksPaginationRes getBooksByCategories (int page, int size, List<String> categories) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        Pageable pageable = PageRequest.of(page, size);
        Page<BookCategory> books = bookCategoryRepository.findAllByCategoriesBookCategoryNameIn(categories, pageable);
        GetBooksPaginationRes res = modelMapper.map(books,GetBooksPaginationRes.class);
        return res;

    }

    public BookInfoRes getBookInfo(Integer bookId) throws NotFoundException {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Books books = bookRepository.findById(bookId).orElseThrow(()->new NotFoundException("book not found"));
        List<String> categories = books.getBookCategories().stream().map((d)->{
            return d.getCategories().getBookCategoryName();
        }).collect(Collectors.toList());
        BookInfoRes res = modelMapper.map(books, BookInfoRes.class);
        res.setCategory(categories);
        return res;
    }

    public List<RequestedBooksRes> getRequestedBooks(Authentication authentication) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<Bookings> bookings = bookingRepository.findAllByUserUserIdAndBookingStatus(userDetails.getUserId(), EBookingStatuses.ALLEGED_TAKING );
        List<RequestedBooksRes> res = bookings.stream().map(d->modelMapper.map(d, RequestedBooksRes.class)).collect(Collectors.toList());
        return res;
    }
    public List<UsedBookRes> getUsedBooks(Authentication authentication) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<Bookings> bookings = bookingRepository.findAllByUserUserIdAndBookingStatus(userDetails.getUserId(), EBookingStatuses.IN_USE );
        List<UsedBookRes> res = bookings.stream().map((d)->{
            return new UsedBookRes(d.getBookingId(),d.getBooks().getBookName(),d.getBooks().getBookImageUrl(),d.getBookingStatus(), Duration.between(LocalDateTime.now(), d.getBookingTime().plusDays(d.getAmountOfDay())).getSeconds());
        }).collect(Collectors.toList());
        return res;
    }

    public CompletedBooksPaginationRes getCompletedBooksOfUser(Authentication authentication, int page, int size) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Pageable pageable = PageRequest.of(page, size);
        Page<Bookings> bookings = bookingRepository.findAllByUserUserIdAndBookingStatusAndShowInHistory(userDetails.getUserId(),EBookingStatuses.COMPLETED, true, pageable);
        List<CompletedUserBooks> completedUserBooks = bookings.stream().map((d)->{
            return new CompletedUserBooks(d.getBookingId(), d.getBooks().getBookImageUrl(), d.getBookingStatus(),d.getBooks().getBookName(),Duration.between(d.getBookingCompletedTime(), d.getBookingTime().plusDays(d.getAmountOfDay())).getSeconds());
        }).collect(Collectors.toList());
        CompletedBooksPaginationRes res = new CompletedBooksPaginationRes(completedUserBooks, bookings.getTotalPages());
        return res;
    }

    public void deleteBook(Integer bookId) {
        bookRepository.deleteById(bookId);
    }





}
