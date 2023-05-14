package com.libaitu.libaitu.service;

import com.libaitu.libaitu.dto.BookInfoRes;
import com.libaitu.libaitu.dto.GetBooksPaginationRes;
import com.libaitu.libaitu.dto.RequestedBooksRes;
import com.libaitu.libaitu.entity.*;
import com.libaitu.libaitu.exception.NotFoundException;
import com.libaitu.libaitu.repository.BookCategoryRepository;
import com.libaitu.libaitu.repository.BookingRepository;
import com.libaitu.libaitu.repository.CategoryRepository;
import com.libaitu.libaitu.repository.BookRepository;
import com.libaitu.libaitu.security.UserDetailsImpl;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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

    public GetBooksPaginationRes getBooks (int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Books> books = bookRepository.findAll(pageable);
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
        Books books = bookRepository.findById(bookId).orElseThrow(()->new NotFoundException("book not found"));
        BookInfoRes res = modelMapper.map(books, BookInfoRes.class);
        return res;
    }

    public List<RequestedBooksRes> getRequestedBooks(Authentication authentication) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<Bookings> bookings = bookingRepository.findAllByUserUserIdAndBookingStatus(userDetails.getUserId(), EBookingStatuses.ALLEGED_TAKING );
        List<RequestedBooksRes> res = bookings.stream().map(d->modelMapper.map(d, RequestedBooksRes.class)).collect(Collectors.toList());
        return res;
    }





}
