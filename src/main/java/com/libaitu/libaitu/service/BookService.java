package com.libaitu.libaitu.service;

import com.libaitu.libaitu.dto.BookInfoRes;
import com.libaitu.libaitu.dto.GetBooksPaginationRes;
import com.libaitu.libaitu.entity.BookCategory;
import com.libaitu.libaitu.entity.Books;
import com.libaitu.libaitu.entity.Categories;
import com.libaitu.libaitu.exception.NotFoundException;
import com.libaitu.libaitu.repository.BookCategoryRepository;
import com.libaitu.libaitu.repository.CategoryRepository;
import com.libaitu.libaitu.repository.BookRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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





}
